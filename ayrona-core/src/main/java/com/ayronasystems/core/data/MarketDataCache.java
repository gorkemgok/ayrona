package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.SymbolPeriod;
import com.ayronasystems.core.util.Interval;

import java.util.*;

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
            List<Interval> absentList =  marketData.getInterval ().extractNot (interval);
            if (interval.equals (marketData.getInterval ())){
                return new MarketDataCacheResult (marketData, absentList);
            }else if (marketData.getInterval ().contains (interval)){
                return new MarketDataCacheResult (
                        marketData.subData (interval.getBeginningDate (), interval.getEndingDate ()),
                                            absentList);
            }else if (interval.contains (marketData.getInterval ())){
                return new MarketDataCacheResult (
                        marketData,
                        absentList
                );
            }else if (interval.overlapsAtLeft (marketData.getInterval ())){
                return new MarketDataCacheResult (
                        marketData.subData (marketData.getBeginningDate (), interval.getEndingDate ()),
                        absentList
                );
            }else if (interval.overlapsAtRight (marketData.getInterval ())){
                return new MarketDataCacheResult (
                        marketData.subData (interval.getBeginningDate (), marketData.getEndingDate ()),
                        absentList
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
