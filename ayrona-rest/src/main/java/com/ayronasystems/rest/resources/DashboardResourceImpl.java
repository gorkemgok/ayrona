package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.StrategyStat;
import com.ayronasystems.rest.bean.DashboardBean;
import com.ayronasystems.rest.resources.definition.DashboardResource;

import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 14/06/16.
 */
public class DashboardResourceImpl implements DashboardResource{

    private Dao dao = Singletons.INSTANCE.getDao ();

    public Response get () {
        StrategyStat strategyStat = dao.getStrategyStat ();
        long accountCount = dao.getAccountCount ();
        return Response.ok (new DashboardBean (strategyStat, accountCount)).build ();
    }
}
