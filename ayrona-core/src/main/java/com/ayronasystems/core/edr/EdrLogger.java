package com.ayronasystems.core.edr;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.concurrent.QueueRunnable;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.EdrModel;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class EdrLogger implements QueueRunnable<Edr>{

    private long edrCount;

    private static EdrLogger edrLogger = new EdrLogger();

    private Dao dao = Singletons.INSTANCE.getDao();

    private EdrLogger(){
    }

    public static EdrLogger getLogger(){
        return edrLogger;
    }

    public void save(Edr edr){
        EdrModel edrModel = EdrModel.valueOf(edr);
        dao.createEdr(edrModel);
        edrCount++;
    }

    public void success(Edr edr){
        edr.status = EdrStatus.SUCCESSFUL;
        save(edr);
    }

    public void error(Edr edr){
        edr.status = EdrStatus.FAILED;
        save(edr);
    }

    public void process(Edr element) {
        save(element);
    }

    public long getEdrCount() {
        return edrCount;
    }
}
