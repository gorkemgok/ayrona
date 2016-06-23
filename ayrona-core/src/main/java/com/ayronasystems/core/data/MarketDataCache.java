package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.SymbolPeriod;
import com.ayronasystems.core.util.Interval;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 11/06/16.
 */
public class MarketDataCache {

    private Map<SymbolPeriod, MarketData> marketDataMap;

    public MarketDataCache () {
        this.marketDataMap = new HashMap<SymbolPeriod, MarketData> ();
    }

    public MarketDataCacheResult search(SymbolPeriod symbolPeriod, Interval interval){
        MarketData marketData = search(symbolPeriod);
        if (marketData != null){
            if (interval.equals (marketData.getInterval ())){
                return new MarketDataCacheResult (marketData, Collections.EMPTY_LIST);
            }else if (marketData.getInterval ().contains (interval)){
                return new MarketDataCacheResult (
                        marketData.subData (interval.getBeginningDate (), interval.getEndingDate ()),
                                            Collections.EMPTY_LIST);
            }else if (interval.contains (marketData.getInterval ())){
                Interval absentInterval1 = new Interval (interval.getBeginningDate (), marketData.getBeginningDate ());
                Interval absentInterval2 = new Interval (marketData.getEndingDate (), interval.getEndingDate ());
                return new MarketDataCacheResult (
                        marketData,
                        Arrays.asList (absentInterval1, absentInterval2)
                );
            }else if (interval.overlapsAtLeft (marketData.getInterval ())){
                Interval absentInterval = new Interval (interval.getBeginningDate (), marketData.getBeginningDate ());
                return new MarketDataCacheResult (
                        marketData.subData (marketData.getBeginningDate (), interval.getEndingDate ()),
                        Arrays.asList (absentInterval)
                );
            }else if (interval.overlapsAtRight (marketData.getInterval ())){
                Interval absentInterval = new Interval (marketData.getEndingDate (), interval.getEndingDate ());
                return new MarketDataCacheResult (
                        marketData.subData (interval.getBeginningDate (), marketData.getEndingDate ()),
                        Arrays.asList (absentInterval)
                );
            }
        }
        return MarketDataCacheResult.NO_DATA;
    }

    public void cache(MarketData marketData){
        SymbolPeriod symbolPeriod = new SymbolPeriod (marketData.getSymbol (), marketData.getPeriod ());
        MarketData thisMarketData = search(symbolPeriod);
        MarketData newMarketData = null;
        if (thisMarketData != null && marketData != null) {
            newMarketData = thisMarketData.safeMerge(marketData);
        }else{
            newMarketData = marketData;
        }
        if (newMarketData != null && !newMarketData.equals(thisMarketData)) {
            synchronized (this) {
                marketDataMap.put(symbolPeriod, newMarketData);
            }
        }
    }

    private synchronized MarketData search(SymbolPeriod symbolPeriod){
        MarketData marketData = null;
        synchronized (this) {
            if (marketDataMap.containsKey(symbolPeriod)) {
                marketData = marketDataMap.get(symbolPeriod);
            }
        }
        return marketData;
    }

}
