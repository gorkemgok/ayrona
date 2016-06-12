package com.ayronasystems.rest.resources.definition;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 11/06/16.
 */
@Path ("/ate")
public interface ATEResource {

    @GET
    @Path ("strategy/list")
    @Produces(MediaType.APPLICATION_JSON)
    Response getStrategyList();

}
