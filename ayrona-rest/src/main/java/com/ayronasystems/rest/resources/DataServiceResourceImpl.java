package com.ayronasystems.rest.resources;

import com.ayronasystems.rest.resources.definition.DataServiceResource;

import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class DataServiceResourceImpl implements DataServiceResource{

    public Response checkHealth () {
        return Response.ok ().build ();
    }
}
