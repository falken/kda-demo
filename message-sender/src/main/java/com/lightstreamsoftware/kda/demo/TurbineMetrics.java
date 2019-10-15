package com.lightstreamsoftware.kda.demo;

import java.util.ArrayList;
import java.util.List;

public class TurbineMetrics {
    private String turbineId;
    private double rotorSpeed;
    private double rotorHeading;
    private double powerOutput;
    private long metricTimestamp;
    private List<Integer> errorCodes = new ArrayList<>();


    public TurbineMetrics(){}

    public TurbineMetrics(String turbineId,double rotorSpeed,double rotorHeading,double powerOutput,List<Integer> errorCodes,long metricTimestamp){
        this.turbineId = turbineId;
        this.rotorSpeed = rotorSpeed;
        this.rotorHeading = rotorHeading;
        this.powerOutput = powerOutput;
        this.errorCodes = errorCodes;
    }

    public String getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(String turbineId) {
        this.turbineId = turbineId;
    }

    public double getRotorSpeed() {
        return rotorSpeed;
    }

    public void setRotorSpeed(double rotorSpeed) {
        this.rotorSpeed = rotorSpeed;
    }

    public double getRotorHeading() {
        return rotorHeading;
    }

    public void setRotorHeading(double rotorHeading) {
        this.rotorHeading = rotorHeading;
    }

    public double getPowerOutput() {
        return powerOutput;
    }

    public void setPowerOutput(double powerOutput) {
        this.powerOutput = powerOutput;
    }

    public List<Integer> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<Integer> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public long getMetricTimestamp() {
        return metricTimestamp;
    }

    public void setMetricTimestamp(long metricTimestamp) {
        this.metricTimestamp = metricTimestamp;
    }
}
