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
public class GrowingStrategyOHLC extends StrategyOHLC {

    public GrowingStrategyOHLC(Symbol symbol, Period period, List<Date> dates, double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) throws CorruptedMarketDataException {
        super(symbol, period, dates, openSeries, highSeries, lowSeries, closeSeries);
    }

    public void prepareForNextData() {
        double[] newOpenSeries = new double[openSeries.length + 1];
        double[] newHighSeries = new double[highSeries.length + 1];
        double[] newLowSeries = new double[lowSeries.length + 1];
        double[] newCloseSeries = new double[closeSeries.length + 1];
        for ( int i = 0; i < openSeries.length; i++ ) {
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

    public static StrategyOHLC valueOf(MarketData marketData) throws CorruptedMarketDataException {
        StrategyOHLC strategyOHLC = new GrowingStrategyOHLC(
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
