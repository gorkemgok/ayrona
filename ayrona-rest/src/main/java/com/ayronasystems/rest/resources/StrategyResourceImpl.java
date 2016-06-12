package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.backtest.MarketSimulator;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.rest.bean.AccountBean;
import com.ayronasystems.rest.bean.AccountBinderBean;
import com.ayronasystems.rest.bean.ErrorBean;
import com.ayronasystems.rest.bean.StrategyBean;
import com.ayronasystems.rest.resources.definition.StrategyResource;
import org.mozilla.javascript.EcmaError;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class StrategyResourceImpl implements StrategyResource {

    private Dao dao = Singletons.INSTANCE.getDao ();

    public Response createStrategy (StrategyBean strategyBean) {
        StrategyModel strategyModel = strategyBean.toStrategyModel ();
        dao.createStrategy (strategyModel);
        return Response.ok (StrategyBean.valueOf (strategyModel)).build ();
    }

    public Response updateStrategy (StrategyBean strategyBean) {

        return null;
    }

    public Response getList () {
        List<StrategyModel> strategyModelList = dao.findAllStrategies ();
        List<StrategyBean> strategyBeanList = new ArrayList<StrategyBean> (strategyModelList.size ());
        for (StrategyModel strategyModel : strategyModelList){
            strategyBeanList.add (StrategyBean.valueOf (strategyModel));
        }
        return Response.ok (strategyBeanList).build ();
    }

    public Response get (String id) {
        return null;
    }

    public Response getBoundAccountList (String id) {
        List<AccountModel> accountModelList = dao.findBoundAccounts (id);
        List<AccountBean> accountBeanList = new ArrayList<AccountBean> (accountModelList.size ());
        for (AccountModel accountModel : accountModelList){
            accountBeanList.add (AccountBean.valueOf (accountModel));
        }
        return Response.ok (accountBeanList).build ();
    }

    public Response getHistory (String id) {
        return null;
    }

    public Response compile (String code) {
        try {
            Algo.createInstance (code);
            return Response.ok ().build ();
        }catch ( EcmaError e ){
            return Response.status (Response.Status.BAD_REQUEST)
                           .entity (new ErrorBean (ErrorBean.STRATEGY_COMPILATION_ERROR, e.getMessage ())).build ();
        }
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
