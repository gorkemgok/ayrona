package com.ayronasystems.rest.security;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by gorkemgok on 23/01/16.
 */
public class AuthContext implements SecurityContext {

    private UserInfo userInfo;

    public AuthContext (UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserPrincipal () {
        return userInfo;
    }

    public boolean isUserInRole (String s) {
        return true;
    }

    public boolean isSecure () {
        return true;
    }

    public String getAuthenticationScheme () {
        return null;
    }
}
