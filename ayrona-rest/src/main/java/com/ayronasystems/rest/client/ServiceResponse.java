package com.ayronasystems.rest.client;

import org.jboss.resteasy.client.jaxrs.internal.ClientResponse;

import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 01/02/16.
 */
public class ServiceResponse {

    private Response rawResponse;

    public ServiceResponse (Response rawResponse) {
        this.rawResponse = rawResponse;
        rawResponse.getStatusInfo ();
    }

    public <T> T parse(Class<T> responseClass){
        ClientResponse clientResponse = (ClientResponse)rawResponse;
        return (T)clientResponse.readEntity (responseClass);
    }
}
