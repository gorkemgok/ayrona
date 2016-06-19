package com.ayronasystems.rest.client;

import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.rest.resources.definition.ATEResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

/**
 * Created by gorkemgok on 01/02/16.
 */
public class ServiceRest {

    private static Logger log = LoggerFactory.getLogger (ServiceRest.class);

    private static Configuration conf = Configuration.getInstance ();

    private String token;

    private ATEResource ateResource;

    public static final ServiceRest INSTANCE = new ServiceRest ();

    private  ServiceRest () {
        ResteasyClient client = new ResteasyClientBuilder ().register (new ClientRequestFilter () {
            public void filter (ClientRequestContext clientRequestContext) throws IOException {
                clientRequestContext.getHeaders ()
                                    .add ("token", token);
            }
        }).build ();
        ateResource = client.target (conf.getString (ConfKey.ATE_REST_URL)).proxy (ATEResource.class);
    }

    public String getToken () {
        return token;
    }

    public ServiceRest setToken (String token) {
        this.token = token;
        return this;
    }

    public ATEResource getAteEndpoint () {
        return ateResource;
    }
}
