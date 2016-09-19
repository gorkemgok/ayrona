package com.ayronasystems.rest.resources.definition;

import com.ayronasystems.rest.bean.OptimizerSessionBean;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 17/09/16.
 */
@Path ("/session")
public interface StrategySessionResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    Response createSession(OptimizerSessionBean optimizerSessionBean);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    Response list(@QueryParam ("page") int page, @QueryParam ("item") int item);

}
