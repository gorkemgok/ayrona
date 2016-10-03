package com.ayronasystems.rest.resources.definition;

import com.ayronasystems.rest.bean.AccountBinderBean;
import com.ayronasystems.rest.bean.BackTestBean;
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

    @DELETE
    @Path ("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response deleteStrategy(@PathParam ("id") String strategyId);

    @GET
    @Path ("list")
    @Produces(MediaType.APPLICATION_JSON)
    Response getList();

    @GET
    @Path ("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response get(@PathParam ("id") String id);

    @GET
    @Path ("{id}/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    Response getBoundAccountList(@PathParam ("id") String id);

    @GET
    @Path ("{id}/history")
    @Produces(MediaType.APPLICATION_JSON)
    Response getHistory(@PathParam ("id") String id);

    @POST
    @Path("compile")
    @Produces(MediaType.APPLICATION_JSON)
    Response compile(String code);

    @POST
    @Path ("backtest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response doBackTest(BackTestBean backTestBean, @QueryParam ("detailed") Boolean isDetailed);

    @POST
    @Path ("{id}/account")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response addAccount(@PathParam ("id") String strategyId, AccountBinderBean accountBinderBean);

    @PUT
    @Path ("{id}/account/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateAccountBound (@PathParam ("id") String strategyId, AccountBinderBean accountBinderBean);

    @DELETE
    @Path ("{id}/account/{aid}")
    @Produces(MediaType.APPLICATION_JSON)
    Response deleteAccount(@PathParam ("id") String id, @PathParam ("aid") String accountId);

}
