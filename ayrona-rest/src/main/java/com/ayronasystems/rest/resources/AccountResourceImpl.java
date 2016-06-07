package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.rest.bean.AccountBean;
import com.ayronasystems.rest.bean.PrerequisiteBean;
import com.ayronasystems.rest.bean.Prerequisites;
import com.ayronasystems.rest.resources.definition.AccountResource;
import com.google.common.base.Optional;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AccountResourceImpl implements AccountResource {

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
            return Response.ok (accountModelOptional.get ()).build ();
        }else {
            return Response.noContent ().build ();
        }
    }

    public Response delete (String id) {
        dao.deleteAccount (id);
        return Response.noContent ().build ();
    }

    public Response getBoundStrategyList (String id) {
        return null;
    }

    public Response getHistory (Long id) {
        return null;
    }

    public Response createAccount (AccountBean accountBean) {
        AccountModel accountModel = accountBean.toAccountModel ();
        PrerequisiteBean prb = Prerequisites.SAVE_ACCOUNT.check (accountBean);
        if (prb.isOk ()){
            dao.createAccount (accountModel);
            return Response.ok (AccountBean.valueOf (accountModel)).build ();
        }else {
            return Response.status (Response.Status.BAD_REQUEST).entity (prb).build ();
        }
    }

    public Response updateAccount (AccountBean accountBean) {
        return null;
    }
}
