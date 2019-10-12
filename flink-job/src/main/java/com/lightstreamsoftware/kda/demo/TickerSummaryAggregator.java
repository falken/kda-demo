package com.lightstreamsoftware.kda.demo;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickerSummaryAggregator implements AggregateFunction<TickerData, TickerSummary,TickerSummary> {
    private static final Logger logger = LoggerFactory.getLogger(TickerSummaryAggregator.class);
    @Override
    public TickerSummary createAccumulator() {
        return new TickerSummary();
    }

    @Override
    public TickerSummary add(TickerData tickerData, TickerSummary acc) {
        return acc.withTickerData(tickerData);
    }

    @Override
    public TickerSummary getResult(TickerSummary acc) {
        return acc;
    }

    @Override
    public TickerSummary merge(TickerSummary acc, TickerSummary acc1) {
        return acc.withTickerSummary(acc1);
    }
}
