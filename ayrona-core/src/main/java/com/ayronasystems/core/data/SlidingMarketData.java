package com.ayronasystems.core.data;

import com.ayronasystems.core.timeseries.moment.Bar;

/**
 * Created by gorkemgok on 29/05/16.
 */
public interface SlidingMarketData extends MarketData{

    void addNewBar(Bar bar);

    void overwriteLastBar(Bar bar);

    void slideSeries();
}
