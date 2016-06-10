package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.CorruptedMarketDataException;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class SlidingStrategyOHLC extends StrategyOHLC{

    public SlidingStrategyOHLC(Symbol symbol, Period period, List<Date> dates, double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) throws CorruptedMarketDataException {
        super(symbol, period, dates, openSeries, highSeries, lowSeries, closeSeries);
    }

    public void prepareForNextData() {
        double[] newOpenSeries = new double[openSeries.length];
        double[] newHighSeries = new double[highSeries.length];
        double[] newLowSeries = new double[lowSeries.length];
        double[] newCloseSeries = new double[closeSeries.length];
        for ( int i = 0; i < openSeries.length - 1; i++ ) {
            newOpenSeries[i] = openSeries[i + 1];
            newHighSeries[i] = highSeries[i + 1];
            newLowSeries[i] = lowSeries[i + 1];
            newCloseSeries[i] = closeSeries[i + 1];
        }
        openSeries = newOpenSeries;
        highSeries = newHighSeries;
        lowSeries = newLowSeries;
        closeSeries = newCloseSeries;
    }

    public static StrategyOHLC valueOf(MarketData marketData) throws CorruptedMarketDataException {
        StrategyOHLC strategyOHLC = new SlidingStrategyOHLC (
                marketData.getSymbol (),
                marketData.getPeriod (),
                marketData.getDates (),
                marketData.getData (PriceColumn.OPEN),
                marketData.getData (PriceColumn.HIGH),
                marketData.getData (PriceColumn.LOW),
                marketData.getData (PriceColumn.CLOSE)
        );
        return strategyOHLC;
    }

}
