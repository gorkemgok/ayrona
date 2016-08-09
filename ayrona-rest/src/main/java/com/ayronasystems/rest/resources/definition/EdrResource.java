package com.ayronasystems.rest.resources.definition;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 02/08/16.
 */
@Path ("/edr")
public interface EdrResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path ("/")
    Response getEdrList(@QueryParam ("module") String module,
                        @QueryParam ("type") String type, @QueryParam ("startDate") String startDateString);

}
