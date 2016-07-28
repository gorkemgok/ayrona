package com.ayronasystems.core.algo.tree;

import com.ayronasystems.core.algo.FIOExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 24/07/16.
 */
public class FIOTable {

    private Map<String, FIOExchange> table = new HashMap<String, FIOExchange> ();

    public void add(String hash, FIOExchange fioExchange){
        table.put (hash, fioExchange);
    }

    public Map<String, FIOExchange> getTable () {
        return new HashMap<String, FIOExchange> (table);
    }

    public void clear(){
        table.clear ();
    }
}
