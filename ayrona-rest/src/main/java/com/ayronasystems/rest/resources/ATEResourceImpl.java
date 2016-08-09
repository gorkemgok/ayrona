package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinder;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.edr.Edr;
import com.ayronasystems.rest.resources.definition.ATEResource;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 * Created by gorkemgok on 28/07/16.
 */
public class ATEResourceImpl implements ATEResource{

    private static Logger log = LoggerFactory.getLogger (ATEResourceImpl.class);

    private Dao dao = Singletons.INSTANCE.getDao ();

    public Response getStrategyList () {
        return null;
    }

    public Response stopStrategy (String strategyId) {
        Optional<StrategyModel> strategyModelOptional = dao.findStrategy (strategyId);
        if (strategyModelOptional.isPresent ()){
            StrategyModel strategyModel = strategyModelOptional.get ();
            strategyModel.setState (AccountBinder.State.INACTIVE);
            if (dao.updateStrategy (strategyModel)){
                log.info ("Stopped strategy {}, id : {}", strategyModel.getName (), strategyModel.getId ());
                Edr.stopStrategy ().success ().strategy (strategyModel.getName ())
                   .strategyId (strategyModel.getId ()).putQueue ();
                return Response.ok ().build ();
            } else {
                return Response.notModified ().build ();
            }
        }
        return Response.noContent ().build ();
    }

    public Response startStrategy (String strategyId) {
        Optional<StrategyModel> strategyModelOptional = dao.findStrategy (strategyId);
        if (strategyModelOptional.isPresent ()){
            StrategyModel strategyModel = strategyModelOptional.get ();
            strategyModel.setState (AccountBinder.State.ACTIVE);
            if (dao.updateStrategy (strategyModel)){
                log.info ("Started strategy {}, id : {}", strategyModel.getName (), strategyModel.getId ());
                Edr.startStrategy ().success ().strategy (strategyModel.getName ())
                        .strategyId (strategyModel.getId ()).putQueue ();
                return Response.ok ().build ();
            } else {
              return Response.notModified ().build ();
            }
        }
        return Response.noContent ().build ();
    }

    public Response stopAccount (String strategyId, String accountId) {
        return null;
    }

    public Response startAccount (String strategyId, String accountId, double lot) {
        return null;
    }
}
