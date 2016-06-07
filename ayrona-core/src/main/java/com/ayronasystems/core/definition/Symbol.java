package com.ayronasystems.core.definition;

/**
 * Created by gorkemgok on 12/03/15.
 */
public enum Symbol {
    EURUSD("EUR","USD"),
    USDTRY("USD","TRY"),
    VOB30("VOB30"),
    GRNT("GARANTI"),
    TEST("TEST");

    private String base;
    private String secondary;

    Symbol(String base, String secondary) {
        this.base = base;
        this.secondary = secondary;
    }

    Symbol(String base) {
        this.base = base;
        this.secondary = "";
    }

    public String getSymbolString(){
        return base+secondary;
    }

    public static boolean hasSymbol(String symbolString){
        for (Symbol symbol : Symbol.values ()){
            if (symbol.getSymbolString ().equals (symbolString)){
                return true;
            }
        }
        return false;
    }
}
