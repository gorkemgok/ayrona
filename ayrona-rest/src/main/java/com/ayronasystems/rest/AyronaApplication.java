package com.ayronasystems.rest;

import com.ayronasystems.rest.resources.AccountResourceImpl;
import com.ayronasystems.rest.resources.AuthResourceImpl;
import com.ayronasystems.rest.resources.MarketDataResourceImpl;
import com.ayronasystems.rest.resources.StrategyResourceImpl;
import com.ayronasystems.rest.security.TokenManager;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaApplication extends Application {

    private Set<Object> resources = new HashSet<Object> ();

    public AyronaApplication () {
        resources.add (new StrategyResourceImpl ());
        resources.add (new AuthResourceImpl (TokenManager.getInstance ()));
        resources.add (new AccountResourceImpl ());
        resources.add (new MarketDataResourceImpl ());
    }

    @Override
    public Set<Object> getSingletons () {
        return resources;
    }

}
