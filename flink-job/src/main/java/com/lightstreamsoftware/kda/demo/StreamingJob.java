package com.lightstreamsoftware.kda.demo;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.util.Properties;

public class StreamingJob {

    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setAutoWatermarkInterval(100);

        Properties inputProperties = new Properties();
        inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, "us-east-1");
        inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "TRIM_HORIZON");
        inputProperties.setProperty(ConsumerConfigConstants.SHARD_GETRECORDS_INTERVAL_MILLIS, "500");
        inputProperties.setProperty(ConsumerConfigConstants.SHARD_GETRECORDS_MAX, "1000");

        DataStream<String> stream = env.addSource(new FlinkKinesisConsumer<>("input-stream", new SimpleStringSchema(), inputProperties))
                .name("Input Source")
                .uid("5f20ef89-e50c-41d8-a75d-909e2b3b98e3")
                .setParallelism(1)
                .setMaxParallelism(1);

        DataStream<TurbineMetrics> tickerData = stream.map(stringBody -> objectMapper.readValue(stringBody, TurbineMetrics.class))
                .name("Parse Json")
                .uid("6d73824f-8b1b-4022-b3ea-f0e406629462")
                .setParallelism(1)
                .setMaxParallelism(1)
                .assignTimestampsAndWatermarks(new TimestampAndWatermarker());

        DataStream<TurbineMetricsSummary> fullAggregations = tickerData
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(1)))
                .aggregate(new TurbineSummaryAggregator("Totals"))
                .name("Aggregate Full Data By 1 Minute")
                .uid("7d528fd2-0d7a-4703-9d80-9bbb419fb78e")
                .setParallelism(1)
                .setMaxParallelism(1);

        DataStream<TurbineMetricsSummary> aggregatedTickerData = tickerData.keyBy(TurbineMetrics::getTurbineId)
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                .aggregate(new TurbineSummaryAggregator("Per Turbine"))
                .name("Aggregate By 1 Minute")
                .uid("5fafb567-53ed-4230-8f70-34d22424a4d3")
                .setParallelism(1)
                .setMaxParallelism(1);

        DataStream<String> resultData = aggregatedTickerData.map(objectMapper::writeValueAsString)
                .name("Output As Json String")
                .uid("5e6fcded-0323-4649-8f0d-6a111eec9499")
                .setParallelism(1)
                .setMaxParallelism(1);

        DataStream<String> fullResultData = fullAggregations.map(objectMapper::writeValueAsString)
                .name("Output Full Agg As Json String")
                .uid("ed393f55-7b05-45ab-b492-296fecee202c")
                .setParallelism(1)
                .setMaxParallelism(1);

        Properties outputProperties = new Properties();
        outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, "us-east-1");
        outputProperties.setProperty("AggregationEnabled", "false");

        FlinkKinesisProducer<String> sinkFunction = new FlinkKinesisProducer<>(new SimpleStringSchema(), outputProperties);
        sinkFunction.setDefaultStream("output-stream");
        sinkFunction.setDefaultPartition("0");

        fullResultData.addSink(sinkFunction)
                .name("Full Aggregate Output Sink")
                .uid("2856b77b-cdbf-4d37-8035-64a16511b5fb")
                .setParallelism(1);

        resultData.addSink(sinkFunction)
                .name("Output Sink")
                .uid("4fa1eca6-f13a-4751-887f-53f2aadecebf")
                .setParallelism(1);

        env.execute("Flink Processor");
    }
}
