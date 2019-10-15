package com.lightstreamsoftware.kda.demo;

import org.apache.flink.api.common.functions.AggregateFunction;

public class TurbineSummaryAggregator implements AggregateFunction<TurbineMetrics, TurbineMetricsBatch, TurbineMetricsBatch> {

    @Override
    public TurbineMetricsBatch createAccumulator() {
        return new TurbineMetricsBatch();
    }

    @Override
    public TurbineMetricsBatch add(TurbineMetrics turbineMetrics, TurbineMetricsBatch acc) {
        return acc.withTurbineMetrics(turbineMetrics);
    }

    @Override
    public TurbineMetricsBatch getResult(TurbineMetricsBatch acc) {

        return acc;
    }

    @Override
    public TurbineMetricsBatch merge(TurbineMetricsBatch acc, TurbineMetricsBatch acc1) {
        return acc.withTurbineMetricsSummary(acc1);
    }
}
