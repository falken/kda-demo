package com.lightstreamsoftware.kda.demo;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.joda.time.DateTime;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

public class TurbineMetricWindowAssigner extends WindowAssigner<Object, TimeWindow> {
    @Override
    public Collection<TimeWindow> assignWindows(Object element, long timestamp, WindowAssignerContext context) {
        TurbineMetrics turbineMetric = (TurbineMetrics) element;
        DateTime arrivalTime = new DateTime(turbineMetric.getMetricTimestamp());

        DateTime minuteFloor = arrivalTime.minuteOfHour().roundFloorCopy();
        DateTime minuteCeiling = minuteFloor.plusMinutes(1).minusMillis(1);

        return newArrayList(new TimeWindow(minuteFloor.getMillis(),minuteCeiling.getMillis()));
    }

    @Override
    public Trigger<Object, TimeWindow> getDefaultTrigger(StreamExecutionEnvironment env) {
        return EventTimeTrigger.create();
    }

    @Override
    public TypeSerializer<TimeWindow> getWindowSerializer(ExecutionConfig executionConfig) {
        return TypeInformation.of(TimeWindow.class).createSerializer(executionConfig);
    }

    @Override
    public boolean isEventTime() {
        return true;
    }
}
