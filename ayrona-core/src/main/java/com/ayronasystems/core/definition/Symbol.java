package com.ayronasystems.core.definition;

import java.io.Serializable;

/**
 * Created by gorkemgok on 12/03/15.
 */
public class Symbol implements Serializable{

    private String name;
    private String code;

    Symbol (String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName () {
        return name;
    }

    public String getCode () {
        return code;
    }

    @Override
    public boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass () != o.getClass () ) {
            return false;
        }

        Symbol symbol = (Symbol) o;

        return name.equals (symbol.name);

    }

    @Override
    public int hashCode () {
        return name.hashCode ();
    }

    @Override
    public String toString () {
        return name;
    }
}
