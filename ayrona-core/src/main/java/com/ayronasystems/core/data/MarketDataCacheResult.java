package com.ayronasystems.core.data;

import com.ayronasystems.core.util.Interval;

import java.util.List;

/**
 * Created by gorkemgok on 11/06/16.
 */
public class MarketDataCacheResult {

    public static final MarketDataCacheResult NO_DATA = new MarketDataCacheResult (null, null);

    private boolean isFoundData;

    private MarketData foundData;

    private List<Interval> absentIntervals;

    public MarketDataCacheResult (MarketData foundData, List<Interval> absentIntervals) {
        this.foundData = foundData;
        this.absentIntervals = absentIntervals;
        this.isFoundData = foundData != null;
    }

    public boolean isFoundData () {
        return isFoundData;
    }

    public MarketData getFoundData () {
        return foundData;
    }

    public List<Interval> getAbsentIntervals () {
        return absentIntervals;
    }
}
