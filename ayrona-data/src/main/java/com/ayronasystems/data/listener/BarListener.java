package com.ayronasystems.data.listener;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;

/**
 * Created by gorkemgok on 10/12/15.
 */
public interface BarListener{
    void newBar (Symbol symbol, Period period, Bar bar);
}
