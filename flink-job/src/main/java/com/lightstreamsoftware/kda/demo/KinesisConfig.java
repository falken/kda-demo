package com.lightstreamsoftware.kda.demo;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.util.Properties;

public class KinesisConfig {

    public static FlinkKinesisConsumer<String> getFlinkSource(String streamName) {
        Properties inputProperties = new Properties();
        inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, "us-east-1");
        inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "LATEST");
        inputProperties.setProperty(ConsumerConfigConstants.SHARD_GETRECORDS_INTERVAL_MILLIS, "500");
        inputProperties.setProperty(ConsumerConfigConstants.SHARD_GETRECORDS_MAX, "1000");
        return new FlinkKinesisConsumer<>(streamName, new SimpleStringSchema(), inputProperties);
    }

    public static FlinkKinesisProducer<String> getFlinkSink(String streamName){
        Properties outputProperties = new Properties();
        outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, "us-east-1");
        outputProperties.setProperty("AggregationEnabled", "false");

        FlinkKinesisProducer<String> sinkFunction = new FlinkKinesisProducer<>(new SimpleStringSchema(), outputProperties);
        sinkFunction.setDefaultStream(streamName);
        sinkFunction.setDefaultPartition("0");
        return sinkFunction;
    }
}
