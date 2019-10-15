package com.lightstreamsoftware.kda.demo;

public class TurbineMetricsSummary {

    private int turbineCount;
    private double totalOutput;
    private double minOutput;
    private double maxOutput;

    public TurbineMetricsSummary(){

    }

    public TurbineMetricsSummary(TurbineMetricsSummary other){
        this.turbineCount = other.turbineCount;
        this.totalOutput = other.totalOutput;
        this.minOutput = other.minOutput;
        this.maxOutput = other.maxOutput;
    }

    public int getTurbineCount() {
        return turbineCount;
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

    public TurbineMetricsSummary withTurbineMetrics(TurbineMetrics turbineMetrics) {
        TurbineMetricsSummary summary = new TurbineMetricsSummary(this);
        summary.totalOutput = turbineMetrics.getPowerOutput() + totalOutput;
        summary.turbineCount = turbineCount + 1;
        summary.minOutput = minOutput > turbineMetrics.getPowerOutput() ? turbineMetrics.getPowerOutput() : minOutput;
        summary.maxOutput = maxOutput > turbineMetrics.getPowerOutput() ? turbineMetrics.getPowerOutput() : maxOutput;
        return summary;
    }

    public TurbineMetricsSummary withTurbineMetricsSummary(TurbineMetricsSummary acc1) {
        TurbineMetricsSummary summary = new TurbineMetricsSummary(this);
        summary.totalOutput = acc1.totalOutput + totalOutput;
        summary.turbineCount = turbineCount + acc1.turbineCount;
        summary.minOutput = minOutput > acc1.getMinOutput() ? acc1.getMinOutput() : minOutput;
        summary.maxOutput = maxOutput > acc1.getMaxOutput() ? acc1.getMaxOutput() : maxOutput;
        return summary;
    }
}
