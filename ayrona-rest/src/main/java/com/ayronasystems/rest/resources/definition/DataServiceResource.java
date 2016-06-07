package com.ayronasystems.rest.resources.definition;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 07/06/16.
 */
@Path ("/")
public interface DataServiceResource {

    @GET
    @Path ("health")
    public Response checkHealth();
}
