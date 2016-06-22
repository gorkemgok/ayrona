package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinder;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.rest.bean.*;
import com.ayronasystems.rest.resources.definition.AccountResource;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AccountResourceImpl implements AccountResource {

    private static Logger log = LoggerFactory.getLogger (AccountResourceImpl.class);

    private Dao dao = Singletons.INSTANCE.getDao ();

    public Response getList () {
        List<AccountModel> accountModelList = dao.findAllAccounts ();
        List<AccountBean> accountBeanList = new ArrayList<AccountBean> (accountModelList.size ());
        for (AccountModel accountModel : accountModelList){
            accountBeanList.add (AccountBean.valueOf (accountModel));
        }
        return Response.ok (accountBeanList).build ();
    }

    public Response get (String id) {
        Optional<AccountModel> accountModelOptional = dao.findAccount (id);
        if (accountModelOptional.isPresent ()){
            return Response.ok (AccountBean.valueOf (accountModelOptional.get ())).build ();
        }else {
            return Response.noContent ().build ();
        }
    }

    public Response delete (String id) {
        dao.deleteAccount (id);
        log.info ("Deleted account {}", id);
        return Response.noContent ().build ();
    }

    public Response getBoundStrategyList (String id) {
        List<StrategyModel> strategyModelList = dao.findBoundStrategies (id);
        List<BoundStrategyBean> boundStrategyBeanList = new ArrayList<BoundStrategyBean> (strategyModelList.size ());
        for (StrategyModel strategyModel : strategyModelList){
            StrategyBean strategyBean = StrategyBean.valueOf (strategyModel);
            AccountBinder.State state = AccountBinder.State.INACTIVE;
            double lot = 0;
            for ( AccountBinder accountBinder : strategyModel.getAccounts ()){
                if (accountBinder.getId ().equals (id)){
                    state = accountBinder.getState ();
                    lot = accountBinder.getLot ();
                }
            }
            boundStrategyBeanList.add (new BoundStrategyBean (state, lot, strategyBean));
        }
        return Response.ok (boundStrategyBeanList).build ();
    }

    public Response getHistory (Long id) {
        return null;
    }

    public Response createAccount (AccountBean accountBean) {
        PrerequisiteCheck check = Prerequisites.SAVE_ACCOUNT.check (accountBean);
        if (check.isOk ()){
            AccountModel accountModel = accountBean.toAccountModel ();
            dao.createAccount (accountModel);
            log.info ("{} Account created with name {} and account number {}", accountModel.getType (), accountModel.getAccountantName (), accountModel.getLoginDetail ().getId ());
            return Response.ok (AccountBean.valueOf (accountModel)).build ();
        } else {
            log.info ("Account prerequisite error : {}", check);
            return Response.status (Response.Status.BAD_REQUEST).entity (check.toBean ()).build ();
        }
    }

    public Response updateAccount (AccountBean accountBean) {
        PrerequisiteCheck check = Prerequisites.SAVE_ACCOUNT.check (accountBean);
        if (check.isOk ()){
            AccountModel accountModel = accountBean.toAccountModel ();
            dao.updateAccount (accountModel);
            log.info ("{} Account updated with name {} and account number {}", accountModel.getType (), accountModel.getAccountantName (), accountModel.getLoginDetail ().getId ());
            return Response.ok (AccountBean.valueOf (accountModel)).build ();
        }else {
            log.info ("Account prerequisite error : {}", check);
            return Response.status (Response.Status.BAD_REQUEST).entity (check.toBean ()).build ();
        }
    }
}
