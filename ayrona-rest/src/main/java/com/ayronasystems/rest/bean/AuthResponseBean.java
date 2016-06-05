package com.ayronasystems.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gorkemgok on 23/01/16.
 */
@XmlRootElement
public class AuthResponseBean {

    private String token;

    public AuthResponseBean (String token) {
        this.token = token;
    }

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }
}
