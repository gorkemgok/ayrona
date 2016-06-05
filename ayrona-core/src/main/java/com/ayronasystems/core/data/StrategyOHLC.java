package com.ayronasystems.core.data;

import com.ayronasystems.core.data.OHLC;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 28/05/16.
 */
public class StrategyOHLC extends OHLC implements SlidingMarketData {

    public StrategyOHLC (Symbol symbol, Period period, List<Date> dates, double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) throws CorruptedMarketDataException {
        super (symbol, period, dates, openSeries, highSeries, lowSeries, closeSeries);
    }

    public void addNewBar(Bar bar){
        slideSeries ();
        overwriteLastBar (bar);
    }

    public void overwriteLastBar(Bar bar){
        openSeries[openSeries.length - 1] = bar.get (ColumnDefinition.OPEN);
        highSeries[highSeries.length - 1] = bar.get (ColumnDefinition.HIGH);
        lowSeries[lowSeries.length - 1] = bar.get (ColumnDefinition.LOW);
        closeSeries[closeSeries.length - 1] = bar.get (ColumnDefinition.CLOSE);
        if (dates.remove (0) != null) {
            dates.add (bar.getDate ());
        }
    }

    public void slideSeries(){
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
        StrategyOHLC strategyOHLC = new StrategyOHLC (
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
