package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.EdrModel;
import com.ayronasystems.core.edr.EdrModule;
import com.ayronasystems.core.edr.EdrType;
import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.rest.resources.definition.EdrResource;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 02/08/16.
 */
public class EdrResourceImpl implements EdrResource {

    private Dao dao = Singletons.INSTANCE.getDao ();

    public Response getEdrList (String module, String type, String startDateString) {
        Date startDate = DateUtils.convertFromISO (startDateString);
        List<EdrModel> edrModelList;
        if (type != null || !type.isEmpty ()){
            edrModelList = dao.findEdr (EdrModule.STRATEGY, startDate, new Date());
        }else if (module != null || !module.isEmpty ()){
            edrModelList = dao.findEdr (EdrType.valueOf (type), startDate, new Date());
        }else{
            edrModelList = dao.findEdr (startDate, new Date());
        }
        return Response.ok (edrModelList).build ();
    }

}
