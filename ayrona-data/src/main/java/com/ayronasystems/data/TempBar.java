package com.ayronasystems.data;

import com.ayronasystems.core.timeseries.moment.Bar;

import java.util.Date;

/**
 * Created by gorkemgok on 01/06/16.
 */
public class TempBar{

    private double open = 0;

    private double high = 0;

    private double low = Double.MAX_VALUE;

    private double close = 0;

    private long periodMillis = 0;

    private boolean isClosed = true;

    public boolean isClosed () {
        return isClosed;
    }

    public void beginPeriod (long periodMillis, double open) {
        this.periodMillis = periodMillis;
        this.open = open;
        setNewPrice (open);
    }

    public void setNewPrice (double price) {
        this.high = Math.max (this.high, price);
        this.low = Math.min (this.low, price);
        this.close = price;
        isClosed = false;
    }

    public Bar endPeriod () {
        Bar bar = new Bar (new Date (periodMillis), open, high, low, close, 0);
        reset ();
        return bar;
    }

    public long getPeriodMillis () {
        return periodMillis;
    }

    public void setPeriodMillis (long periodMillis) {
        this.periodMillis = periodMillis;
    }

    private void reset(){
        open = 0;
        high = 0;
        low = Double.MAX_VALUE;
        close = 0;
        periodMillis = 0;
        isClosed = true;
    }
}
