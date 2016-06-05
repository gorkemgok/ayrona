package com.ayronasystems.rest.security;

import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.rest.bean.ErrorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by gorkemgok on 23/01/16.
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static Logger log = LoggerFactory.getLogger (SecurityFilter.class);

    private static Configuration conf = Configuration.getInstance ();

    public void filter (ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo ().getPath ();
        if (!path.equals ("/login")) {
            String token = requestContext.getHeaderString ("token");
            String lordOfTheToken = conf.getString (ConfKey.LORD_OF_THE_TOKEN);
            if ( token != null ){
                if ( token.equals (lordOfTheToken) ) {
                    UserInfo userInfo = new UserInfo (token, null, "0", "God");
                    requestContext.setSecurityContext (new AuthContext (userInfo));
                } else if ( TokenManager.getInstance ().checkToken (token) ) {
                    UserInfo userInfo = TokenManager.getInstance ()
                                                    .getTokenInfo (token);
                    requestContext.setSecurityContext (new AuthContext (userInfo));
                } else {
                    requestContext.abortWith (Response.status (Response.Status.UNAUTHORIZED)
                                                               .entity (new ErrorBean (ErrorBean.ERR_UNAUTHORIZED,
                                                                                       "Unauthorized token"))
                                                               .build ());
                }
            }else{
                requestContext.abortWith (Response.status (Response.Status.UNAUTHORIZED)
                                                           .entity (new ErrorBean (ErrorBean.ERR_UNAUTHORIZED,
                                                                                   "Cant find token"))
                                                           .build ());
            }

        }
    }
}
