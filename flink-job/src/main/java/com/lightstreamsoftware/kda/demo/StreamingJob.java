package com.lightstreamsoftware.kda.demo;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;

public class StreamingJob {

    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(KinesisConfig.getFlinkSource("input-stream"))
                .map(stringBody -> objectMapper.readValue(stringBody, TurbineMetrics.class))
                .windowAll(TumblingProcessingTimeWindows.of(Time.minutes(1)))
                .aggregate(new TurbineSummaryAggregator())
                .map(objectMapper::writeValueAsString)
                .addSink(KinesisConfig.getFlinkSink("output-stream"));

        env.execute("Flink Processor");
    }
}
