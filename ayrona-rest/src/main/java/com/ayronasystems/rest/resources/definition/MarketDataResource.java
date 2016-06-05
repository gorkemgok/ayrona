package com.ayronasystems.rest.resources.definition;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 04/06/16.
 */
@Path ("/marketdata")
public interface MarketDataResource {

    @POST
    @Path ("import")
    @Consumes("multipart/form-data")
    Response importCsv(MultipartFormDataInput input, @QueryParam ("symbol") String symbol);

}
