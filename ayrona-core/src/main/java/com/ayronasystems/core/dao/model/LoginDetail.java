package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by gorkemgok on 05/06/16.
 */
@Embedded
public class LoginDetail {

    private String id;

    private String password;

    private String server;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getServer () {
        return server;
    }

    public void setServer (String server) {
        this.server = server;
    }
}
