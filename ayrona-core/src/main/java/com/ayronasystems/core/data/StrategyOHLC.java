package com.ayronasystems.core.data;

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
public abstract class StrategyOHLC extends OHLC implements StrategyMarketData {

    public enum Type{
        SLIDING,
        GROWING
    }

    public StrategyOHLC (Symbol symbol, Period period, List<Date> dates, double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) throws CorruptedMarketDataException {
        super (symbol, period, dates, openSeries, highSeries, lowSeries, closeSeries);
    }

    public void addNewBar(Bar bar){
        prepareForNextData ();
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

    public abstract void prepareForNextData();
}
