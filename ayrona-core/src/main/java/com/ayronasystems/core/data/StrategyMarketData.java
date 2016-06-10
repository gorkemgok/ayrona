package com.ayronasystems.core.data;

import com.ayronasystems.core.timeseries.moment.Bar;

/**
 * Created by gorkemgok on 29/05/16.
 */
public interface StrategyMarketData extends MarketData{

    void addNewBar(Bar bar);

    void overwriteLastBar(Bar bar);

    void prepareForNextData();
}
