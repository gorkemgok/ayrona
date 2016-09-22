package com.ayronasystems.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
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

    private static class BarTimer extends Thread{

        private Barifier barifier;

        private Symbol symbol;

        private long timerStep;

        private long remainingTime;

        private boolean wait;

        public BarTimer (Barifier barifier, Symbol symbol, long timerStep) {
            this.barifier = barifier;
            this.symbol = symbol;
            this.timerStep = timerStep;
        }

        public void setToWaitMode(){
            wait = true;
            remainingTime = 0;
        }
        public void setRemainingTime (long remainingTime) {
            this.remainingTime = remainingTime;
            wait = false;
        }

        public void run(){
            while ( !isInterrupted () ){
                if (!wait && remainingTime <= 0){
                    wait = true;
                    remainingTime = 0;
                    barifier.timeoutBar (symbol);
                }
                try {
                    sleep (timerStep);
                } catch ( InterruptedException e ) {
                    break;
                }
                remainingTime -= timerStep;
            }
        }
    }

    private static Logger log = LoggerFactory.getLogger (Barifier.class);

    private Map<Symbol, TempBar> tempStack = new HashMap<Symbol, TempBar> ();

    private Map<Symbol, TimeSeries<Bar>> seriesStack = new HashMap<Symbol, TimeSeries<Bar>> ();

    private Map<Symbol, BarTimer> barTimerStack = new HashMap<Symbol, BarTimer> ();

    private List<BarListener> barListenerList = new ArrayList<BarListener> ();

    private final Period period;

    public Barifier (Period period) {
        this.period = period;
    }

    public void newTick(LiveTick tick){
        log.debug ("New Tick : {}, {}, {}", tick.getSymbol (), tick.getDate (), tick.getBid ());
        long timeMillis = tick.getDate ().getTime ();
        long periodMillis = timeMillis - (timeMillis % period.getAsMillis ());
        long remainingMillis = periodMillis + period.getAsMillis () - timeMillis;
        TempBar tempBar = getOrCreate (tick.getSymbol ());
        BarTimer barTimer = getOrCreateTimer (tick.getSymbol ());
        if (tempBar.isClosed() && periodMillis > tempBar.getPeriodMillis ()){
            if (!tick.isDirty ()) {
                tempBar.beginPeriod (periodMillis, tick.getBid ());
            }
        }else{
            if (tempBar.getPeriodMillis () != periodMillis){
                Symbol symbol = tick.getSymbol ();
                Bar bar = tempBar.endPeriod ();
                newBar (symbol, bar);
                if (!tick.isDirty ()){
                    tempBar.beginPeriod (periodMillis, tick.getBid ());
                }
            }else {
                if (!tick.isDirty ()) {
                    tempBar.setNewPrice (tick.getBid ());
                }
            }
        }
        if (!tick.isDirty ()) {
            barTimer.setRemainingTime (remainingMillis);
        }else{
            barTimer.setToWaitMode ();
        }
        //tempBar.setPeriodMillis (periodMillis);
    }

    private void timeoutBar(Symbol symbol){
        TempBar tempBar = getOrCreate (symbol);
        Bar bar = tempBar.endPeriod ();
        newBar (symbol, bar);
    }

    private void newBar (Symbol symbol, Bar bar) {
        getOrCreateSeries (symbol).addMoment (bar);
        log.info ("Added {} bar to timeseries: {}", symbol, bar);
        for (BarListener barListener : barListenerList){
            barListener.newBar (symbol, period, bar);
        }
    }

    private TempBar getOrCreate(Symbol symbol){
        TempBar tempBar = tempStack.get (symbol);
        if (tempBar == null){
            tempBar = new TempBar ();
            tempStack.put (symbol, tempBar);
        }
        return tempBar;
    }

    private BarTimer getOrCreateTimer(Symbol symbol){
        BarTimer barTimer = barTimerStack.get (symbol);
        if (barTimer == null){
            barTimer = new BarTimer (this, symbol, 100);
            barTimer.start ();
            barTimerStack.put (symbol, barTimer);
        }
        return barTimer;
    }

    private TimeSeries<Bar> getOrCreateSeries(Symbol symbol){
        TimeSeries<Bar> timeSeries = seriesStack.get (symbol);
        if (timeSeries == null){
            timeSeries = new SymbolTimeSeries<Bar> (symbol, period, Bar.class);
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
