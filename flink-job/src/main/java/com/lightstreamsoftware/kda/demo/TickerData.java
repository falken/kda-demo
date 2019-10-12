package com.lightstreamsoftware.kda.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TickerData {
    @JsonProperty("PRICE")
    private double price;

    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("EVENT_TIME")
    private String eventTime;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
