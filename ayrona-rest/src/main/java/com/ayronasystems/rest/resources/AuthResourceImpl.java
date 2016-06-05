package com.ayronasystems.rest.resources;

import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.UserModel;
import com.ayronasystems.rest.bean.AuthResponseBean;
import com.ayronasystems.rest.bean.ErrorBean;
import com.ayronasystems.rest.resources.definition.AuthResource;
import com.ayronasystems.rest.security.TokenManager;
import com.ayronasystems.rest.security.UserInfo;
import com.google.common.base.Optional;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.UUID;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AuthResourceImpl implements AuthResource{

    private static Logger log = LoggerFactory.getLogger (AuthResourceImpl.class);

    private Dao dao;

    private TokenManager tokenManager;

    @Context
    SecurityContext securityContext;

    public AuthResourceImpl (Dao dao, TokenManager tokenManager) {
        this.dao = dao;
        this.tokenManager = tokenManager;
    }

    public Response login (String authString) {
        String decodedAutInfo = new String (Base64.decodeBase64(authString));
        String[] decodedAutInfoSplit = decodedAutInfo.split (":");
        if ( decodedAutInfoSplit.length == 2 ) {
            String login = decodedAutInfoSplit[0];
            String password = decodedAutInfoSplit[1];
            //TODO: check if user is already logon
            Optional<UserModel> userModel = dao.findUserByLogin (login);
            if ( userModel.isPresent () && userModel.get ().getPassword ().equals (password) ) {
                UUID token = UUID.randomUUID ();
                String tokenString = token.toString ();
                tokenManager.addToken (tokenString, new UserInfo (tokenString, null, userModel.get ().getId (), userModel.get ().getName ()));
                return Response.ok ().entity (new AuthResponseBean (tokenString)).build ();
            }
        }

        return Response.status (Response.Status.UNAUTHORIZED).entity (new ErrorBean (ErrorBean.ERR_UNAUTHORIZED, "User name or password is incorrect")).build ();
    }

    public Response logout () {
        UserInfo userInfo = (UserInfo)securityContext.getUserPrincipal ();
        tokenManager.removeToken (userInfo.getToken ());
        return Response.ok ().build ();
    }
}
