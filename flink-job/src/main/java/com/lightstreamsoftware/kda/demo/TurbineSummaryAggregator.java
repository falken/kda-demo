package com.lightstreamsoftware.kda.demo;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TurbineSummaryAggregator implements AggregateFunction<TurbineMetrics, TurbineMetricsSummary,TurbineMetricsSummary> {
    private static final Logger logger = LoggerFactory.getLogger(TurbineSummaryAggregator.class);
    private String metricType;

    public TurbineSummaryAggregator(String metricType) {
        this.metricType = metricType;
    }

    @Override
    public TurbineMetricsSummary createAccumulator() {
        return new TurbineMetricsSummary(metricType);
    }

    @Override
    public TurbineMetricsSummary add(TurbineMetrics turbineMetrics, TurbineMetricsSummary acc) {
        return acc.withTurbineMetrics(turbineMetrics);
    }

    @Override
    public TurbineMetricsSummary getResult(TurbineMetricsSummary acc) {
        return acc;
    }

    @Override
    public TurbineMetricsSummary merge(TurbineMetricsSummary acc, TurbineMetricsSummary acc1) {
        return acc.withTurbineMetricsSummary(acc1);
    }
}
