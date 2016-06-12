package com.ayronasystems.core.algo;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.SymbolPeriod;
import com.ayronasystems.core.timeseries.moment.Bar;

import java.io.Serializable;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class LiveBar implements Serializable{

    private SymbolPeriod symbolPeriod;

    private Bar bar;

    public LiveBar (Symbol symbol, Period period, Bar bar) {
        this.symbolPeriod = new SymbolPeriod (symbol, period);
        this.bar = bar;
    }

    public SymbolPeriod getSymbolPeriod () {
        return symbolPeriod;
    }

    public Bar getBar () {
        return bar;
    }
}
