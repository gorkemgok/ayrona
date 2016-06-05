package com.ayronasystems.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 01/06/16.
 */
public class Barifier {

    private static final long PERIOD = Period.M1.getAsMillis ();

    private Map<Symbol, TempBar> tempStack = new HashMap<Symbol, TempBar> ();

    private Map<Symbol, TimeSeries<Bar>> seriesStack = new HashMap<Symbol, TimeSeries<Bar>> ();

    private long currentMillis;

    public void newTick(Tick tick){
        long timeMillis = tick.getDate ().getTime ();
        long periodMillis = timeMillis - (timeMillis % PERIOD);
        TempBar tempBar = getOrCreate (tick.getSymbol ());
        if (tempBar.isClosed()){
            tempBar.beginPeriod (periodMillis, tick.getMid ());
        }else{
            if (currentMillis != periodMillis){
                Bar bar = tempBar.endPeriod ();
                getOrCreateSeries (tick.getSymbol ()).addMoment (bar);
                tempBar.beginPeriod (periodMillis, tick.getMid ());
            }else {
                tempBar.setNewPrice (tick.getMid ());
            }
        }
        currentMillis = periodMillis;
    }

    private TempBar getOrCreate(Symbol symbol){
        TempBar tempBar = tempStack.get (symbol);
        if (tempBar == null){
            tempBar = new TempBar ();
            tempStack.put (symbol, tempBar);
        }
        return tempBar;
    }

    private TimeSeries<Bar> getOrCreateSeries(Symbol symbol){
        TimeSeries<Bar> timeSeries = seriesStack.get (symbol);
        if (timeSeries == null){
            timeSeries = new SymbolTimeSeries<Bar> (symbol, Period.M1, Bar.class);
            seriesStack.put (symbol, timeSeries);
        }
        return timeSeries;
    }

    public Optional<TimeSeries<Bar>> getSeries(Symbol symbol){
        TimeSeries<Bar> series = seriesStack.get (symbol);
        if (series != null){
            return Optional.of (series);
        }
        return Optional.absent ();
    }

}
