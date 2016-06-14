package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
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
                StrategyModel strategyModel = strategyBean.toStrategyModel ();
                dao.updateStrategy (strategyModel);
                return Response.ok (StrategyBean.valueOf (strategyModel))
                               .build ();
            }
        }else{
            return Response.status (Response.Status.BAD_REQUEST).entity (check.toBean ()).build ();
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
                                             Symbol.valueOf(backTestBean.getSymbol ()),
                                             Period.valueOf (backTestBean.getPeriod ()),
                                             DateUtils.convertFromISO (backTestBean.getBeginDate ()),
                                             DateUtils.convertFromISO (backTestBean.getEndDate ()));
        return Response.ok (btr).build ();
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
