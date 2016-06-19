package com.ayronasystems.rest.resources.definition;

import javax.ws.rs.*;
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

    @DELETE
    @Path ("strategy/{id}/stop")
    @Produces(MediaType.APPLICATION_JSON)
    Response stopStrategy(@PathParam ("id") String strategyId);

    @POST
    @Path ("strategy/{id}/start")
    @Produces(MediaType.APPLICATION_JSON)
    Response startStrategy(@PathParam ("id") String strategyId);

    @DELETE
    @Path ("strategy/{strategyId}/account/{accountId}/stop")
    @Produces(MediaType.APPLICATION_JSON)
    Response stopAccount(@PathParam ("strategyId") String strategyId, @PathParam ("accountId") String accountId);

    @DELETE
    @Path ("strategy/{strategyId}/account/{accountId}/start")
    @Produces(MediaType.APPLICATION_JSON)
    Response startAccount(@PathParam ("strategyId") String strategyId, @PathParam ("accountId") String accountId, @QueryParam ("lot") double lot);


}
