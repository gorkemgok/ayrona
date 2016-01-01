package com.ayronasystems.core.exception;

import com.ayronasystems.core.definition.Period;

/**
 * Created by gorkemgok on 27/06/16.
 */
public class MarketDataConversionException extends Exception{

    private Period from;

    private Period to;

    public MarketDataConversionException (Period from, Period to) {
        super("Cannot convert "+from+" to "+to);
        this.from = from;
        this.to = to;
    }

    public Period getFrom () {
        return from;
    }

    public Period getTo () {
        return to;
    }
}
