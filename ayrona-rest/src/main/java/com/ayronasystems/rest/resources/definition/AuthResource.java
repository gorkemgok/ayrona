package com.ayronasystems.rest.resources.definition;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Path ("/")
public interface AuthResource {

    @GET
    @Path ("login")
    @Produces(MediaType.APPLICATION_JSON)
    Response login(@HeaderParam ("auth") String authString);

    @GET
    @Path ("logout")
    @Produces(MediaType.APPLICATION_JSON)
    Response logout();

}
