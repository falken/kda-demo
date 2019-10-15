package com.lightstreamsoftware.kda.demo;

import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.Set;

public class TurbineMetricsSummary {
    private String metricType;

    private Set<String> key = new HashSet<>();
    private int readings;
    private double totalOutput;
    private String summaryTimestamp;

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Set<String> getKey() {
        return key;
    }

    public void setKey(Set<String> key) {
        this.key = key;
    }

    public int getReadings() {
        return readings;
    }

    public void setReadings(int readings) {
        this.readings = readings;
    }

    public double getTotalOutput() {
        return totalOutput;
    }

    public void setTotalOutput(double totalOutput) {
        this.totalOutput = totalOutput;
    }

    public String getSummaryTimestamp() {
        return summaryTimestamp;
    }

    public void setSummaryTimestamp(String summaryTimestamp) {
        this.summaryTimestamp = summaryTimestamp;
    }
}
