package com.lightstreamsoftware.kda.demo;

import com.google.common.base.Ticker;

public class TickerSummary {
    private String ticker;
    private double minPrice = Double.MAX_VALUE;
    private double maxPrice = Double.MIN_VALUE;
    private int changeCount = 0;

    public TickerSummary(){

    }

    public TickerSummary(TickerSummary other){
        this.ticker = other.ticker;
        this.minPrice = other.minPrice;
        this.maxPrice = other.maxPrice;
        this.changeCount = other.changeCount;
    }

    public String getTicker() {
        return ticker;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public int getChangeCount() {
        return changeCount;
    }

    public TickerSummary withTickerData(TickerData data){
        TickerSummary returnValue = new TickerSummary(this);
        returnValue.ticker = data.getTicker();
        returnValue.minPrice = data.getPrice() < minPrice?data.getPrice():minPrice;
        returnValue.maxPrice = data.getPrice() > maxPrice?data.getPrice():maxPrice;
        returnValue.changeCount = changeCount + 1;
        return returnValue;
    }

    public TickerSummary withTickerSummary(TickerSummary data){
        TickerSummary returnValue = new TickerSummary(this);
        returnValue.ticker = data.getTicker();
        returnValue.minPrice = data.minPrice < minPrice?data.minPrice:minPrice;
        returnValue.maxPrice = data.maxPrice > maxPrice?data.maxPrice:maxPrice;
        returnValue.changeCount = changeCount + 1;
        return returnValue;
    }
}
