package com.lightstreamsoftware.kda.demo;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

public class TimestampAndWatermarker implements AssignerWithPeriodicWatermarks<TickerData> {

    private long lastRecordProcessingTime;

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(lastRecordProcessingTime);
    }

    @Override
    public long extractTimestamp(TickerData element, long previousElementTimestamp) {
        return lastRecordProcessingTime = DateTime.parse(element.getEventTime()).getMillis();
    }
}
