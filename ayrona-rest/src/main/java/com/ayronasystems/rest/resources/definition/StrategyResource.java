package com.ayronasystems.rest.resources.definition;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Path ("/strategy")
public interface StrategyResource {

    @GET
    @Path ("list")
    @Produces(MediaType.APPLICATION_JSON)
    Response list();

    @GET
    @Path ("{id}/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    Response accounts(@PathParam ("id") Long id);

    @GET
    @Path ("{id}/start")
    @Produces(MediaType.APPLICATION_JSON)
    Response start(@PathParam ("id") Long id, @QueryParam ("account_id") Long accountId);

    @GET
    @Path ("{id}/stop")
    @Produces(MediaType.APPLICATION_JSON)
    Response stop(@PathParam ("id") Long id, @QueryParam ("account_id") Long accountId);

}
