package com.ayronasystems.ate.rest;

import com.ayronasystems.ate.AlgoTradingEngine;
import com.ayronasystems.ate.rest.resource.ATEResourceImpl;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaATEApplication extends Application {

    private Set<Object> resources = new HashSet<Object> ();

    public AyronaATEApplication () {
        resources.add (new ATEResourceImpl (AlgoTradingEngine.INSTANCE));
    }

    @Override
    public Set<Object> getSingletons () {
        return resources;
    }

}
