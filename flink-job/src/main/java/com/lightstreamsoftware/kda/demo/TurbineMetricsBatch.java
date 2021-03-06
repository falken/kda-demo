package com.lightstreamsoftware.kda.demo;

import java.util.HashSet;
import java.util.Set;

public class TurbineMetricsBatch {

    private Set<String> key = new HashSet<>();
    private int readings;
    private double totalOutput;
    private double minOutput = Double.MAX_VALUE;
    private double maxOutput = Double.MIN_VALUE;
    private long minTimestamp = Long.MAX_VALUE;
    private long maxTimestamp = Long.MIN_VALUE;

    public TurbineMetricsBatch() {

    }

    public TurbineMetricsBatch(TurbineMetricsBatch other) {
        this.key = other.key;
        this.readings = other.readings;
        this.totalOutput = other.totalOutput;
        this.minOutput = other.minOutput;
        this.maxOutput = other.maxOutput;
        this.minTimestamp = other.minTimestamp;
    }


    public int getReadings() {
        return readings;
    }

    public int getTurbineCount() {
        return readings;
    }

    public double getTotalOutput() {
        return totalOutput;
    }

    public double getMinOutput() {
        return minOutput;
    }

    public double getMaxOutput() {
        return maxOutput;
    }


    public long getMinTimestamp() {
        return minTimestamp;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public void setMinTimestamp(long minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public void setMaxTimestamp(long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public Set<String> getKey() {
        return key;
    }

    public TurbineMetricsBatch withTurbineMetrics(TurbineMetrics turbineMetrics) {
        TurbineMetricsBatch summary = new TurbineMetricsBatch(this);
        summary.key.add(turbineMetrics.getTurbineId());
        summary.totalOutput = turbineMetrics.getPowerOutput() + totalOutput;
        summary.readings = readings + 1;
        summary.minOutput = minOutput > turbineMetrics.getPowerOutput() ? turbineMetrics.getPowerOutput() : minOutput;
        summary.maxOutput = maxOutput < turbineMetrics.getPowerOutput() ? turbineMetrics.getPowerOutput() : maxOutput;

        summary.minTimestamp = minTimestamp > turbineMetrics.getMetricTimestamp() ? turbineMetrics.getMetricTimestamp() : minTimestamp;
        summary.maxTimestamp = maxTimestamp < turbineMetrics.getMetricTimestamp() ? turbineMetrics.getMetricTimestamp() : maxTimestamp;
        return summary;
    }

    public TurbineMetricsBatch withTurbineMetricsSummary(TurbineMetricsBatch acc1) {
        TurbineMetricsBatch summary = new TurbineMetricsBatch(this);
        summary.key.addAll(key);
        summary.key.addAll(acc1.key);
        summary.totalOutput = acc1.totalOutput + totalOutput;
        summary.readings = readings + acc1.readings;
        summary.minOutput = minOutput > acc1.minOutput ? acc1.minOutput : minOutput;
        summary.maxOutput = maxOutput < acc1.maxOutput ? acc1.maxOutput : maxOutput;
        summary.minTimestamp = minTimestamp > acc1.minTimestamp ? acc1.minTimestamp : minTimestamp;
        summary.maxTimestamp = maxTimestamp < acc1.maxTimestamp ? acc1.maxTimestamp : maxTimestamp;
        return summary;
    }
}
