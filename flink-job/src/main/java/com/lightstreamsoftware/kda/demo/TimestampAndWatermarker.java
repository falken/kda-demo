package com.lightstreamsoftware.kda.demo;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class TimestampAndWatermarker implements AssignerWithPeriodicWatermarks<TurbineMetrics> {

    private long lastRecordProcessingTime;

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(lastRecordProcessingTime);
    }

    @Override
    public long extractTimestamp(TurbineMetrics element, long previousElementTimestamp) {
        return lastRecordProcessingTime = element.getMetricTimestamp();
    }
}
