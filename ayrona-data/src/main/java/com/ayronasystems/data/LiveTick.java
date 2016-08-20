package com.ayronasystems.data;

import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Tick;

import java.util.Date;

/**
 * Created by gorkemgok on 20/08/16.
 */
public class LiveTick extends Tick{

    private boolean isDirty;

    public LiveTick (Date timestamp, Symbol symbol, double ask, double mid, double bid, boolean isDirty) {
        super (timestamp, symbol, ask, mid, bid);
        this.isDirty = isDirty;
    }

    public LiveTick(Tick tick, boolean isDirty){
        this(tick.getDate (), tick.getSymbol (), tick.getAsk (), tick.getMid (), tick.getBid (), isDirty);
    }

    public boolean isDirty () {
        return isDirty;
    }
}
