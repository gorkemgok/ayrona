package com.ayronasystems.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;

import java.io.Closeable;

/**
 * Created by gorkemgok on 10/12/15.
 */
public interface BarListener extends Closeable{
    void newBar (Bar bar, Symbol symbol, Period period);
}
