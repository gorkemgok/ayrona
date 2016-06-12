package com.ayronasystems.rest.resources.definition;

import com.ayronasystems.rest.bean.AccountBinderBean;
import com.ayronasystems.rest.bean.StrategyBean;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Path ("/strategy")
public interface StrategyResource {

    @POST
    @Path ("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response createStrategy(StrategyBean strategyBean);

    @PUT
    @Path ("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateStrategy(StrategyBean strategyBean);

    @GET
    @Path ("list")
    @Produces(MediaType.APPLICATION_JSON)
    Response getList();

    @GET
    @Path ("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response get(String id);

    @GET
    @Path ("{id}/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    Response getBoundAccountList(@PathParam ("id") String id);

    @GET
    @Path ("{id}/history")
    @Produces(MediaType.APPLICATION_JSON)
    Response getHistory(@PathParam ("id") String id);

    @GET
    @Path("compile")
    @Produces(MediaType.APPLICATION_JSON)
    Response compile(String code);

    @GET
    @Path ("{id}/backtest")
    @Produces(MediaType.APPLICATION_JSON)
    Response getBackTest(@PathParam ("id") String id);

    @POST
    @Path ("backtest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response getBackTest(StrategyBean strategyBean);

    @POST
    @Path ("{id}/boundAccount")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response addAccount(AccountBinderBean accountBinderBean);

    @PUT
    @Path ("{id}/boundAccount")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateAccountState(AccountBinderBean accountBinderBean);

    @DELETE
    @Path ("{id}/boundAccount/{aid}")
    @Produces(MediaType.APPLICATION_JSON)
    Response addAccount(@PathParam ("id") String id, @PathParam ("aid") String accountId);

}
