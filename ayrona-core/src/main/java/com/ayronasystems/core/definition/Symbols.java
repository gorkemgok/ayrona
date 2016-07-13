package com.ayronasystems.core.definition;

import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 11/07/16.
 */
public class Symbols {

    private static Configuration conf = Configuration.getInstance ();

    private static Map<String, Symbol> symbolMap = new HashMap<String, Symbol> ();

    public static void init(){
        String[] symbolPairList = conf.getString (ConfKey.SYMBOLS).split (",");
        for (String symbolPair : symbolPairList) {
            String[] symbol = symbolPair.split ("-");
            if (symbol.length > 1) {
                symbolMap.put (symbol[0], new Symbol (symbol[0], symbol[1]));
            }
        }
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
