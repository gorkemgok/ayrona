package com.ayronasystems.rest.resources;

import com.ayronasystems.rest.AyronaApplication;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by gorkemgok on 06/06/16.
 */
public class AccountResourceImplTest {

    private UndertowJaxrsServer server;

    @Before
    public void setup(){
        server = new UndertowJaxrsServer ().start ();
        server.deploy (AyronaApplication.class);
    }

    @Test
    public void testApplicationPath() throws Exception
    {
        Client client = ClientBuilder.newClient();
        String val = client.target(TestPortProvider.generateURL("/test"))
                           .request().get(String.class);
        Assert.assertEquals("hello world", val);
        client.close();
    }
}