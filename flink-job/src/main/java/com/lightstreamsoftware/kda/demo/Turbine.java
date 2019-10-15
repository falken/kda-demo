package com.lightstreamsoftware.kda.demo;

public class Turbine {
    private String turbineId;
    private double longitude;
    private double latitude;

    public Turbine(){}

    public Turbine(String turbineId,double longitude,double latitude){

        this.turbineId = turbineId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(String turbineId) {
        this.turbineId = turbineId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
