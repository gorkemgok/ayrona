package com.ayronasystems.core.definition;

import java.io.Serializable;

/**
 * Created by gorkemgok on 08/06/16.
 */
public class SymbolPeriod implements Serializable{
    private Symbol symbol;

    private Period period;

    public SymbolPeriod (Symbol symbol, Period period) {
        this.symbol = symbol;
        this.period = period;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public Period getPeriod () {
        return period;
    }

    @Override
    public boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass () != o.getClass () ) {
            return false;
        }

        SymbolPeriod that = (SymbolPeriod) o;

        if ( !symbol.equals ( that.symbol ) ) {
            return false;
        }
        return period.equals ( that.period );

    }

    @Override
    public int hashCode () {
        int result = symbol.hashCode ();
        result = 31 * result + period.hashCode ();
        return result;
    }
}
