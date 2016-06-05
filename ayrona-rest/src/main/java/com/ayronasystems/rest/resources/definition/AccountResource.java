package com.ayronasystems.rest.resources.definition;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Path ("account")
public interface AccountResource {

    @GET
    @Path ("list")
    @Produces(MediaType.APPLICATION_JSON)
    Response list();

    @GET
    @Path ("{id}/history")
    @Produces (MediaType.APPLICATION_JSON)
    Response history(@PathParam ("id") Long id);

    @GET
    @Path ("{id}/strategy_list")
    @Produces (MediaType.APPLICATION_JSON)
    Response strategyList(@PathParam ("id") Long id);

    @GET
    @Path ("{id}/start")
    @Produces (MediaType.APPLICATION_JSON)
    Response start(@PathParam ("id") Long id, @QueryParam ("strategy_id") Long strategyId);

    @GET
    @Path ("{id}/stop")
    @Produces (MediaType.APPLICATION_JSON)
    Response stop(@PathParam ("id") Long id, @QueryParam ("strategy_id") Long strategyId);
}
