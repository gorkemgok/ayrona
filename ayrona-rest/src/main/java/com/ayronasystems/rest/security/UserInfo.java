package com.ayronasystems.rest.security;

import java.security.Principal;
import java.util.Date;

/**
 * Created by gorkemgok on 23/01/16.
 */
public class UserInfo implements Principal{

    private String token;

    private Date validUntil;

    private String userId;

    private String userName;

    public UserInfo (String token, Date validUntil, String userId, String userName) {
        this.token = token;
        this.validUntil = validUntil;
        this.userId = userId;
        this.userName = userName;
    }

    public String getToken () {
        return token;
    }

    public Date getValidUntil () {
        return validUntil;
    }

    public String getUserId () {
        return userId;
    }

    public String getName () {
        return userName;
    }
}
