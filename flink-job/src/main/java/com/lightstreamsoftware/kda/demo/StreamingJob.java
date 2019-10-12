package com.lightstreamsoftware.kda.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Properties;

public class StreamingJob {

    private static final Logger logger = LoggerFactory.getLogger(StreamingJob.class);

    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setAutoWatermarkInterval(100);

        Properties inputProperties = new Properties();
        inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, "us-east-1");
        inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "TRIM_HORIZON");
        inputProperties.setProperty(ConsumerConfigConstants.SHARD_GETRECORDS_INTERVAL_MILLIS, "1000");
        inputProperties.setProperty(ConsumerConfigConstants.SHARD_GETRECORDS_MAX, "100");

        DataStream<String> stream = env.addSource(new FlinkKinesisConsumer<>("input-stream", new SimpleStringSchema(), inputProperties))
                .name("Input Source")
                .uid("5f20ef89-e50c-41d8-a75d-909e2b3b98e3")
                .setParallelism(1)
                .setMaxParallelism(1);

        DataStream<TickerData> tickerData = stream.map(stringBody -> objectMapper.readValue(stringBody, TickerData.class))
                .name("Parse Json")
                .uid("6d73824f-8b1b-4022-b3ea-f0e406629462")
                .setParallelism(1)
                .setMaxParallelism(1)
                .assignTimestampsAndWatermarks(new TimestampAndWatermarker());

        DataStream<TickerSummary> aggregatedTickerData = tickerData.keyBy(TickerData::getTicker)
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                .aggregate(new TickerSummaryAggregator())
                .name("Aggregate By 1 Minute")
                .uid("7d528fd2-0d7a-4703-9d80-9bbb419fb78e")
                .setParallelism(1)
                .setMaxParallelism(1);

        DataStream<String> resultData = aggregatedTickerData.map(objectMapper::writeValueAsString)
                .name("Output As Json String")
                .uid("5e6fcded-0323-4649-8f0d-6a111eec9499")
                .setParallelism(1)
                .setMaxParallelism(1);

        Properties outputProperties = new Properties();
        outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, "us-east-1");
        outputProperties.setProperty("AggregationEnabled", "false");

        FlinkKinesisProducer<String> sinkFunction = new FlinkKinesisProducer<>(new SimpleStringSchema(), outputProperties);
        sinkFunction.setDefaultStream("output-stream");
        sinkFunction.setDefaultPartition("0");

        resultData.addSink(sinkFunction)
                .name("Output Sink")
                .uid("4fa1eca6-f13a-4751-887f-53f2aadecebf")
                .setParallelism(1);

        env.execute("Flink Processor");
    }
}
