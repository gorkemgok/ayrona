package com.ayronasystems.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import com.ayronasystems.data.listener.BarListener;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 01/06/16.
 */
public class Barifier {

    private static Logger log = LoggerFactory.getLogger (Barifier.class);

    private static final Period PERIOD = Period.M1;

    private static final long PERIOD_MILLIS = PERIOD.getAsMillis ();

    private Map<Symbol, TempBar> tempStack = new HashMap<Symbol, TempBar> ();

    private Map<Symbol, TimeSeries<Bar>> seriesStack = new HashMap<Symbol, TimeSeries<Bar>> ();

    private List<BarListener> barListenerList = new ArrayList<BarListener> ();

    private long currentMillis;

    public void newTick(Tick tick){
        log.debug ("{}, {}, {}", tick.getSymbol (), tick.getDate (), tick.getBid ());
        long timeMillis = tick.getDate ().getTime ();
        long periodMillis = timeMillis - (timeMillis % PERIOD_MILLIS);
        TempBar tempBar = getOrCreate (tick.getSymbol ());
        if (tempBar.isClosed()){
            tempBar.beginPeriod (periodMillis, tick.getBid ());
        }else{
            if (tempBar.getCurrentMillis () != periodMillis){
                Bar bar = tempBar.endPeriod ();
                getOrCreateSeries (tick.getSymbol ()).addMoment (bar);
                log.info ("Added {} bar to timeseries: {}", tick.getSymbol (), bar);
                for (BarListener barListener : barListenerList){
                    barListener.newBar (tick.getSymbol (), PERIOD, bar);
                }
                tempBar.beginPeriod (periodMillis, tick.getBid ());
            }else {
                tempBar.setNewPrice (tick.getBid ());
            }
        }
        tempBar.setCurrentMillis (periodMillis);
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

    public void addBarListener(BarListener barListener){
        barListenerList.add (barListener);
    }

}
