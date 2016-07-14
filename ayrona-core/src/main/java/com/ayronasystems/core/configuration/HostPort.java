package com.ayronasystems.core.configuration;

import com.ayronasystems.core.exception.IllegalInputException;

/**
 * Created by gorkemgok on 14/07/16.
 */
public class HostPort {

    private String host;

    private int port;

    public HostPort (String hostPort) throws IllegalInputException{
        try {
            String[] hp = hostPort.split (":");
            host = hp[0];
            port = Integer.valueOf (hp[1]);
        }catch ( Exception ex ){
            throw new IllegalInputException (ex.getMessage ());
        }
    }

    public HostPort (String host, int port) {
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
