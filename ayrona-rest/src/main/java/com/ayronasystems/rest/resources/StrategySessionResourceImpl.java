package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.LimitOffset;
import com.ayronasystems.core.dao.PaginatedResult;
import com.ayronasystems.core.dao.model.OptimizerSessionModel;
import com.ayronasystems.rest.bean.OptimizerSessionBean;
import com.ayronasystems.rest.bean.PrerequisiteCheck;
import com.ayronasystems.rest.bean.Prerequisites;
import com.ayronasystems.rest.resources.definition.StrategySessionResource;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 17/09/16.
 */
public class StrategySessionResourceImpl implements StrategySessionResource{

    private Dao dao = Singletons.INSTANCE.getDao ();

    public Response createSession (OptimizerSessionBean optimizerSessionBean) {
        PrerequisiteCheck check = Prerequisites.SAVE_OPT.check (optimizerSessionBean);
        if (check.isOk ()) {
            OptimizerSessionModel optimizerSessionModel = optimizerSessionBean.to ();
            dao.createOptimizerSession (optimizerSessionModel);
            return Response.ok (OptimizerSessionBean.valueOf (optimizerSessionModel)).build ();
        }else{
            return Response.status (Response.Status.BAD_REQUEST).entity (check.toBean ()).build ();
        }
    }

    public Response list (int page, int item) {
        PaginatedResult<OptimizerSessionModel> optimizerSessionModelPaginatedResult = dao.findOptimizerSessions (
                new LimitOffset (page, item)
        );
        List<OptimizerSessionBean> optimizerSessionBeanList = new ArrayList<OptimizerSessionBean> ();
        for (OptimizerSessionModel optimizerSessionModel : optimizerSessionModelPaginatedResult.getList ()){
            optimizerSessionBeanList.add (OptimizerSessionBean.valueOf (optimizerSessionModel));
        }
        PaginatedResult<OptimizerSessionBean> optimizerSessionBeanPaginatedResult =
                new PaginatedResult<OptimizerSessionBean> (
                        optimizerSessionModelPaginatedResult.getCount (),
                        optimizerSessionBeanList
                        );
        return Response.ok (optimizerSessionBeanPaginatedResult).build ();
    }
}
