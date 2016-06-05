package com.ayronasystems.core.timeseries.series;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Moment;

/**
 * Created by gorkemgok on 07/07/15.
 */
public class SymbolTimeSeries<M extends Moment> extends BasicTimeSeries<M> implements TimeSeries<M>{

    private final Symbol symbol;

    private final Period period;

    public SymbolTimeSeries (Symbol symbol, Period period, Class<M> classOfMoment) {
        super(classOfMoment);
        this.symbol = symbol;
        this.period = period;
    }

    public SymbolTimeSeries (Symbol symbol, Period period, Class<M> classOfMoment, ListType listType, int initialSize) {
        super(classOfMoment, listType, initialSize);
        this.symbol = symbol;
        this.period = period;
    }

    @Override
    public void addMoment(M m) {
        super.addMoment(m);
    }

    public static <Mm extends Moment> SymbolTimeSeries<Mm> getInstance(Class<Mm> classOfMoment, Symbol symbol, Period period){
        return new SymbolTimeSeries<Mm>(symbol, period, classOfMoment);
    }

    public static <Mm extends Moment> SymbolTimeSeries<Mm> getDynamicSizeInstance(Class<Mm> classOfMoment, Symbol symbol, Period period, int initialSize){
        return new SymbolTimeSeries<Mm>(symbol, period, classOfMoment, ListType.DYNAMIC_SIZE, initialSize);
    }

    public static <Mm extends Moment> SymbolTimeSeries<Mm> getFixedSizeInstance(Class<Mm> classOfMoment, Symbol symbol, Period period, int initialSize){
        return new SymbolTimeSeries<Mm>(symbol, period, classOfMoment, ListType.FIXED_SIZE, initialSize);
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public Period getPeriod () {
        return period;
    }

}
