package com.ayronasystems.core.algo;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;

import java.io.Serializable;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class LiveBar implements Serializable{

    private Symbol symbol;

    private Period period;

    private Bar bar;

    public LiveBar (Symbol symbol, Period period, Bar bar) {
        this.symbol = symbol;
        this.period = period;
        this.bar = bar;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public Period getPeriod () {
        return period;
    }

    public Bar getBar () {
        return bar;
    }
}
