package com.ayronasystems.rest;

import com.ayronasystems.rest.resources.*;
import com.ayronasystems.rest.security.SecurityFilter;
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
        resources.add (new SecurityFilter ());
        resources.add (new DashboardResourceImpl ());
        resources.add (new StrategyResourceImpl ());
        resources.add (new AuthResourceImpl (TokenManager.getInstance ()));
        resources.add (new AccountResourceImpl ());
        resources.add (new MarketDataResourceImpl ());
        resources.add (new ATEResourceImpl ());
    }

    @Override
    public Set<Object> getSingletons () {
        return resources;
    }

}
