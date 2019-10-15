package com.lightstreamsoftware.kda.demo;


import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();

    public static void main(String[] args) {
        AmazonKinesis kinesis = AmazonKinesisClientBuilder.defaultClient();

        Stream<Integer> infiniteStream = Stream.iterate(0, i -> i + 1);

        List<Turbine> turbineList = new ArrayList<>();

        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());
        turbineList.add(randomTurbine());

        infiniteStream.forEach(App.sendMessage(kinesis, turbineList));

        logger.info("Hello World!");
    }

    private static Turbine randomTurbine() {
        double latitude = 41.492454;
        double longitude = -94.492732;

        return new Turbine(UUID.randomUUID().toString(), latitude, longitude);
    }

    private static Consumer<Integer> sendMessage(AmazonKinesis kinesis, List<Turbine> turbineList) {
        return (value) -> {
            List<PutRecordsRequestEntry> metrics = turbineList.stream()
                    .map(App::generateMetric)
                    .map(App::buildPutMetricsEntry)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            PutRecordsRequest putRecordsRequest = new PutRecordsRequest()
                    .withRecords(metrics)
                    .withStreamName("input-stream");
            kinesis.putRecords(putRecordsRequest);
            logger.info("Sent batch");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    private static Optional<PutRecordsRequestEntry> buildPutMetricsEntry(TurbineMetrics turbineMetrics) {
        try {
            String metricString = objectMapper.writeValueAsString(turbineMetrics);
            return Optional.of(new PutRecordsRequestEntry()
                    .withData(ByteBuffer.wrap(metricString.getBytes(StandardCharsets.UTF_8)))
                    .withPartitionKey(UUID.randomUUID().toString()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static TurbineMetrics generateMetric(Turbine turbine) {

        long timestamp = DateTime.now(DateTimeZone.UTC).getMillis();
        double rotorSpeed = random.doubles(1, 0, 60).findFirst().orElse(0.0d);
        double heading = random.doubles(1, 45, 60).findFirst().orElse(0.0d);
        double powerOutput = random.doubles(1, 0, 60).findFirst().orElse(0.0d);

        return new TurbineMetrics(turbine.getTurbineId(), rotorSpeed, heading, powerOutput, new ArrayList<>(),timestamp);
    }


}
