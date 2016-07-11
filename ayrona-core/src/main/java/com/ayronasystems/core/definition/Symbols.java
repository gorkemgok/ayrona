package com.ayronasystems.core.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 11/07/16.
 */
public class Symbols {

    private static Map<String, Symbol> symbolMap = new HashMap<String, Symbol> ();

    public static void init(){
        symbolMap.put ("FX_EURUSD", new Symbol ("FX_EURUSD", "EURUSD"));
        symbolMap.put ("FX_USDTRY", new Symbol ("FX_USDTRY", "USDTRY"));
        symbolMap.put ("VOB_X30", new Symbol ("VOB_X30", "EURUSD"));
    }

    public static Symbol of(String name){
        if (symbolMap.containsKey (name)){
            return symbolMap.get (name);
        }
        Symbol symbol = new Symbol (name, name);
        symbolMap.put (name, symbol);
        return symbol;
    }

    public static Collection<Symbol> getList(){
        return symbolMap.values ();
    }

    public static boolean hasSymbolCode(String code){
        for ( Symbol symbol : symbolMap.values () ){
            if (symbol.getCode ().equals (code)){
                return true;
            }
        }
        return false;
    }

}
