package com.ayronasystems.rest.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 23/01/16.
 */
public class TokenManager {

    private Map<String, UserInfo> tokenMap =new HashMap<String, UserInfo>();

    private static TokenManager manager = null;

    public static TokenManager getInstance () {
        if ( manager != null ) {
            return manager;
        }
        manager = new TokenManager ();
        return manager;
    }

    private TokenManager () {

    }

    public void addToken (String token, UserInfo userInfo){
        tokenMap.put (token, userInfo);
    }

    public void removeToken(String token){
        tokenMap.remove (token);
    }

    public boolean checkToken(String token){
        return tokenMap.containsKey (token);
    }

    public UserInfo getTokenInfo(String token){
        return tokenMap.get (token);
    }
}
