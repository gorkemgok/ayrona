package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinderModel;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.service.BackTestService;
import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.rest.bean.*;
import com.ayronasystems.rest.resources.definition.StrategyResource;
import com.google.common.base.Optional;
import org.mozilla.javascript.EcmaError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class StrategyResourceImpl implements StrategyResource {

    private static Logger log = LoggerFactory.getLogger (StrategyResourceImpl.class);

    private Dao dao = Singletons.INSTANCE.getDao ();

    private BackTestService bts = Singletons.INSTANCE.getBackTestService ();

    public Response createStrategy (StrategyBean strategyBean) {
        String code = strategyBean.getCode ();
        PrerequisiteCheck check = Prerequisites.SAVE_STRATEGY.check (strategyBean);
        if (check.isOk ()) {
            Response compilationResponse = compile (code);
            if (compilationResponse.getStatus() != 200){
                return compilationResponse;
            }else {
                StrategyModel strategyModel = strategyBean.toStrategyModel ();
                dao.createStrategy (strategyModel);
                return Response.ok (StrategyBean.valueOf (strategyModel))
                               .build ();
            }
        }else{
            return Response.status (Response.Status.BAD_REQUEST).entity (check.toBean ()).build ();
        }
    }

    public Response updateStrategy (StrategyBean strategyBean) {
        String code = strategyBean.getCode ();
        PrerequisiteCheck check = Prerequisites.SAVE_STRATEGY.check (strategyBean);
        if (check.isOk ()) {
            Response compilationResponse = compile (code);
            if (compilationResponse.getStatus() != 200){
                return compilationResponse;
            }else {
                Optional<StrategyModel> strategyModelOptional = dao.findStrategy (strategyBean.getId ());
                if (strategyModelOptional.isPresent ()) {
                    StrategyModel newStrategyModel = strategyBean.toStrategyModel ();
                    dao.updateStrategy (newStrategyModel);
                    return Response.ok (StrategyBean.valueOf (newStrategyModel))
                                   .build ();
                }else{
                    return Response.noContent ().build ();
                }
            }
        }else{
            return Response.status (Response.Status.BAD_REQUEST).entity (check.toBean ()).build ();
        }
    }

    public Response deleteStrategy (String strategyId) {
        boolean deleted = dao.deleteStrategy (strategyId);
        if (deleted) {
            return Response.ok ()
                           .build ();
        }else{
            return Response.noContent ().build ();
        }
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
        Optional<StrategyModel> strategyModelOptional = dao.findStrategy (id);
        if (strategyModelOptional.isPresent ()){
            return Response.ok (StrategyBean.valueOf (strategyModelOptional.get ())).build ();
        }
        return Response.status (Response.Status.NOT_FOUND).build ();
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
            log.info ("Compilation successful.Code: {}", code);
            return Response.ok ().build ();
        }catch ( EcmaError e ){
            log.info ("Compilation error: {}.Code: {}", e.getErrorMessage (), code);
            return Response.status (Response.Status.BAD_REQUEST)
                           .entity (new ErrorBean (ErrorBean.STRATEGY_COMPILATION_ERROR, e.getMessage ())).build ();
        }
    }

    public Response doBackTest (BackTestBean backTestBean) {
        BackTestResult btr = bts.doBackTest (backTestBean.getCode (),
                                             Symbols.of(backTestBean.getSymbol ()),
                                             Period.valueOf (backTestBean.getPeriod ()),
                                             DateUtils.convertFromISO (backTestBean.getBeginDate ()),
                                             DateUtils.convertFromISO (backTestBean.getEndDate ()));
        return Response.ok (BackTestResultBean.valueOf (btr)).build ();
    }

    public Response addAccount (String strategyId, AccountBinderBean accountBinderBean) {
        dao.bindAccountToStrategy (strategyId, accountBinderBean.toAccountBinder ());
        //Response response = ServiceRest.INSTANCE.getAteEndpoint ().startAccount (strategyId, accountBinderBean.getId (), accountBinderBean.getLot ());
        return Response.ok ().build ();
    }

    public Response updateAccountBound (String strategyId, AccountBinderBean accountBinderBean) {
        AccountBinderModel accountBinderModel = accountBinderBean.toAccountBinder ();
        dao.updateBoundAccount (strategyId, accountBinderModel);
        /*if (accountBinder.getState ().equals (AccountBinder.State.ACTIVE)) {
            return ServiceRest.INSTANCE.getAteEndpoint ()
                                .startAccount (strategyId, accountBinder.getId (), accountBinder.getLot ());
        }else{
            return ServiceRest.INSTANCE.getAteEndpoint ()
                                .stopAccount (strategyId, accountBinder.getId ());
        }*/
        return Response.ok ().build ();
    }

    public Response deleteAccount (String id, String accountId) {
        dao.unboundAccount (id, accountId);
        //return ServiceRest.INSTANCE.getAteEndpoint ().stopAccount (id, accountId);
        return Response.ok ().build ();
    }
}
