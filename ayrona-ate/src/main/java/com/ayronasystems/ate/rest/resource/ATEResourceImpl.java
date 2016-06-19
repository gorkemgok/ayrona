package com.ayronasystems.ate.rest.resource;

import com.ayronasystems.ate.AlgoTradingEngine;
import com.ayronasystems.ate.RunningStrategy;
import com.ayronasystems.ate.rest.bean.RunningStrategyBean;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.rest.bean.ErrorBean;
import com.ayronasystems.rest.resources.definition.ATEResource;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 19/06/16.
 */
public class ATEResourceImpl implements ATEResource{

    private static Logger log = LoggerFactory.getLogger (ATEResourceImpl.class);

    private AlgoTradingEngine ate;

    private Dao dao = Singletons.INSTANCE.getDao ();

    public ATEResourceImpl (AlgoTradingEngine ate) {
        this.ate = ate;
    }

    public Response getStrategyList () {
        List<RunningStrategy> runningStrategyList = ate.getRunningStrategyList ();
        List<RunningStrategyBean> runningStrategyBeanList = new ArrayList<RunningStrategyBean> (runningStrategyList.size ());
        for (RunningStrategy runningStrategy : runningStrategyList){
            runningStrategyBeanList.add (RunningStrategyBean.valueOf (runningStrategy));
        }
        return Response.ok (runningStrategyBeanList).build ();
    }

    public Response stopStrategy (String strategyId) {
        try {
            ate.deregisterStrategy (strategyId);
            log.info ("Unregistered strategy {}", strategyId);
        }catch ( Exception e ){
            log.error ("Cant deregister strategy "+strategyId, e);
            return Response.status (Response.Status.INTERNAL_SERVER_ERROR).entity (new ErrorBean (546, e.getMessage ())).build ();
        }
        return Response.ok ().build ();
    }

    public Response startStrategy (String strategyId) {
        try {
            Optional<StrategyModel> strategyModelOptional = dao.findStrategy (strategyId);
            if (strategyModelOptional.isPresent ()){
                StrategyModel strategyModel = strategyModelOptional.get ();
                ate.registerStrategy (strategyModel);
                log.info ("Registered strategy {}", strategyId);
            }else {
                log.info ("Cant find strategy {}", strategyId);
                return Response.noContent ().build ();
            }
        }catch ( Exception e ){
            log.error ("Cant register strategy "+strategyId, e);
            return Response.status (Response.Status.INTERNAL_SERVER_ERROR).entity (new ErrorBean (547, e.getMessage ())).build ();
        }
        return Response.ok ().build ();
    }

    public Response stopAccount (String strategyId, String accountId) {
        try {
            if (ate.unbindAccount (strategyId, accountId)) {
                log.info ("Started account {} at strategy", accountId, strategyId);
            }else {
                log.info ("Cant find strategy {}", strategyId);
            }
            log.info ("Stopped account {} at strategy", accountId, strategyId);
        }catch ( Exception e ){
            log.error ("Cant stop account "+accountId+" at strategy"+strategyId, e);
            return Response.status (Response.Status.INTERNAL_SERVER_ERROR).entity (new ErrorBean (548, e.getMessage ())).build ();
        }
        return Response.ok ().build ();
    }

    public Response startAccount (String strategyId, String accountId, double lot) {
        try {
            Optional<AccountModel> accountModelOptional = dao.findAccount (accountId);
            if (accountModelOptional.isPresent ()) {
                AccountModel accountModel = accountModelOptional.get ();
                Account account = new BasicAccount (
                    accountModel.getId (),
                    accountModel.getAccountantName (),
                    BasicAccount.createAccountRemoteUsing (accountModel)
                );
                if (ate.bindAccount (strategyId, account, lot)) {
                    log.info ("Started account {} at strategy", accountId, strategyId);
                }else {
                    log.info ("Cant find strategy {}", strategyId);
                }
            }else {
                log.info ("Cant find account {}", accountId);
            }
        }catch ( Exception e ){
            log.error ("Cant start account "+accountId+" at strategy"+strategyId, e);
            return Response.status (Response.Status.INTERNAL_SERVER_ERROR).entity (new ErrorBean (548, e.getMessage ())).build ();
        }
        return Response.ok ().build ();
    }
}
