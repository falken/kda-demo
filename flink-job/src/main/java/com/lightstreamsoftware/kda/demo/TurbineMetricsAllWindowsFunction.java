package com.lightstreamsoftware.kda.demo;

import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.joda.time.DateTime;

public class TurbineMetricsAllWindowsFunction extends ProcessAllWindowFunction<TurbineMetricsBatch, TurbineMetricsSummary, TimeWindow> {

    @Override
    public void process(Context context, Iterable<TurbineMetricsBatch> elements, Collector<TurbineMetricsSummary> out) {
        TurbineMetricsBatch batchSummary = new TurbineMetricsBatch();
        for (TurbineMetricsBatch element : elements) {
            batchSummary = batchSummary.withTurbineMetricsSummary(element);
        }

        TurbineMetricsSummary summary = new TurbineMetricsSummary();
        summary.setKey(batchSummary.getKey());
        summary.setMetricType("Totals");
        summary.setReadings(batchSummary.getReadings());
        summary.setTotalOutput(batchSummary.getTotalOutput());
        summary.setSummaryTimestamp(new DateTime(context.window().getStart()).plusMinutes(1).toString());

        out.collect(summary);
    }
}
