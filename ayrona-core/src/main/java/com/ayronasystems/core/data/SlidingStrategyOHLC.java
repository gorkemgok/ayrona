package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.CorruptedMarketDataException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class SlidingStrategyOHLC extends StrategyOHLC{

    private final int size;

    public SlidingStrategyOHLC(Symbol symbol, Period period, List<Date> dates, double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) throws CorruptedMarketDataException {
        super(symbol, period, dates, openSeries, highSeries, lowSeries, closeSeries);
        size = dates.size ();
    }

    public SlidingStrategyOHLC(Symbol symbol, Period period, int size) throws CorruptedMarketDataException {
        super(symbol, period, new ArrayList<Date> (), new double[0], new double[0], new double[0], new double[0]);
        this.size = size;
    }

    public void prepareForNextData() {
        if (openSeries.length == size) {
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
        }else{
            double[] newOpenSeries = new double[openSeries.length + 1];
            double[] newHighSeries = new double[highSeries.length + 1];
            double[] newLowSeries = new double[lowSeries.length + 1];
            double[] newCloseSeries = new double[closeSeries.length + 1];
            for ( int i = 0; openSeries.length > i; i++ ) {
                newOpenSeries[i] = openSeries[i];
                newHighSeries[i] = highSeries[i];
                newLowSeries[i] = lowSeries[i];
                newCloseSeries[i] = closeSeries[i];
            }
            openSeries = newOpenSeries;
            highSeries = newHighSeries;
            lowSeries = newLowSeries;
            closeSeries = newCloseSeries;
        }
        if (!dates.isEmpty ()){
            dates.remove (0);
        }
    }

    public static StrategyOHLC valueOf(MarketData marketData) throws CorruptedMarketDataException {
        StrategyOHLC strategyOHLC = new SlidingStrategyOHLC (
                marketData.getSymbol (),
                marketData.getPeriod (),
                marketData.getDates (),
                marketData.getPrice (PriceColumn.OPEN),
                marketData.getPrice (PriceColumn.HIGH),
                marketData.getPrice (PriceColumn.LOW),
                marketData.getPrice (PriceColumn.CLOSE)
        );
        return strategyOHLC;
    }

}
