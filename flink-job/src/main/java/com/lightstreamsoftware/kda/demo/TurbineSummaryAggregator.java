package com.lightstreamsoftware.kda.demo;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.joda.time.DateTime;

public class TurbineSummaryAggregator implements AggregateFunction<TurbineMetrics, TurbineMetricsBatch, TurbineMetricsSummary> {

    private String metricType;

    public TurbineSummaryAggregator(String metricType) {
        this.metricType = metricType;
    }

    @Override
    public TurbineMetricsBatch createAccumulator() {
        return new TurbineMetricsBatch();
    }

    @Override
    public TurbineMetricsBatch add(TurbineMetrics turbineMetrics, TurbineMetricsBatch acc) {
        return acc.withTurbineMetrics(turbineMetrics);
    }

    @Override
    public TurbineMetricsSummary getResult(TurbineMetricsBatch acc) {
        TurbineMetricsSummary summary = new TurbineMetricsSummary();
        summary.setKey(acc.getKey());
        summary.setMetricType(metricType);
        summary.setReadings(acc.getReadings());
        summary.setTotalOutput(acc.getTotalOutput());
        summary.setSummaryTimestamp(new DateTime(acc.getMinTimestamp()).plusMinutes(1).toString());
        return summary;
    }

    @Override
    public TurbineMetricsBatch merge(TurbineMetricsBatch acc, TurbineMetricsBatch acc1) {
        return acc.withTurbineMetricsSummary(acc1);
    }
}
