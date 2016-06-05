package com.ayronasystems.core.service.discovery;

/**
 * Created by gorkemgok on 04/06/16.
 */
public class ServiceInfo {

    private String host;

    private int port;

    public ServiceInfo (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost () {
        return host;
    }

    public int getPort () {
        return port;
    }
}
