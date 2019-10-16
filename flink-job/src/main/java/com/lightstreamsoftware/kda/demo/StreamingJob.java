package com.lightstreamsoftware.kda.demo;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;

public class StreamingJob {

    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setAutoWatermarkInterval(100);

        DataStream<TurbineMetrics> tickerData = env.addSource(KinesisConfig.getFlinkSource("input-stream"))
                .map(stringBody -> objectMapper.readValue(stringBody, TurbineMetrics.class))
                .assignTimestampsAndWatermarks(new TimestampAndWatermarker());

        FlinkKinesisProducer<String> sinkFunction = KinesisConfig.getFlinkSink("output-stream");

        tickerData.keyBy(TurbineMetrics::getTurbineId)
                .window(new TurbineMetricWindowAssigner())
                .aggregate(new TurbineSummaryAggregator(), new TurbineMetricsWindowFunction())
                .map(objectMapper::writeValueAsString)
                .addSink(sinkFunction);

        tickerData.windowAll(new TurbineMetricWindowAssigner())
                .aggregate(new TurbineSummaryAggregator(), new TurbineMetricsAllWindowsFunction())
                .map(objectMapper::writeValueAsString)
                .addSink(sinkFunction);

        env.execute("Flink Processor");
    }
}
