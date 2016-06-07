package com.ayronasystems.rest.resources.definition;

import com.ayronasystems.rest.bean.AccountBean;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Path ("/account")
public interface AccountResource {

    @GET
    @Path ("list")
    @Produces(MediaType.APPLICATION_JSON)
    Response getList();

    @GET
    @Path ("{id}")
    @Produces (MediaType.APPLICATION_JSON)
    Response get(@PathParam ("id") String id);

    @DELETE
    @Path ("{id}")
    @Produces (MediaType.APPLICATION_JSON)
    Response delete(@PathParam ("id") String id);

    @GET
    @Path ("{id}/strategies")
    @Produces (MediaType.APPLICATION_JSON)
    Response getBoundStrategyList(@PathParam ("id") String id);

    @GET
    @Path ("{id}/history")
    @Produces (MediaType.APPLICATION_JSON)
    Response getHistory(@PathParam ("id") Long id);

    @POST
    @Path ("")
    @Produces (MediaType.APPLICATION_JSON)
    @Consumes (MediaType.APPLICATION_JSON)
    Response createAccount(AccountBean accountBean);

    @PUT
    @Path ("")
    @Produces (MediaType.APPLICATION_JSON)
    @Consumes (MediaType.APPLICATION_JSON)
    Response updateAccount(AccountBean accountBean);

}
