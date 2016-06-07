package com.ayronasystems.data;

import com.ayronasystems.rest.resources.DataServiceResourceImpl;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaDSApplication extends Application {

    private Set<Object> resources = new HashSet<Object> ();

    public AyronaDSApplication () {
        resources.add (new DataServiceResourceImpl ());
    }

    @Override
    public Set<Object> getSingletons () {
        return resources;
    }

}
