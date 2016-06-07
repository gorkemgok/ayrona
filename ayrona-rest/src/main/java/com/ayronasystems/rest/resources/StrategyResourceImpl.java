package com.ayronasystems.rest.resources;

import com.ayronasystems.rest.bean.AccountBinderBean;
import com.ayronasystems.rest.bean.StrategyBean;
import com.ayronasystems.rest.resources.definition.StrategyResource;

import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class StrategyResourceImpl implements StrategyResource {

    public Response createStrategy (StrategyBean strategyBean) {
        return null;
    }

    public Response updateStrategy (StrategyBean strategyBean) {
        return null;
    }

    public Response getList () {
        return null;
    }

    public Response get (String id) {
        return null;
    }

    public Response getBoundAccountList (String id) {
        return null;
    }

    public Response getHistory (String id) {
        return null;
    }

    public Response getBackTest (String id) {
        return null;
    }

    public Response getBackTest (StrategyBean strategyBean) {
        return null;
    }

    public Response addAccount (AccountBinderBean accountBinderBean) {
        return null;
    }

    public Response updateAccountState (AccountBinderBean accountBinderBean) {
        return null;
    }

    public Response addAccount (String id, String accountId) {
        return null;
    }
}
