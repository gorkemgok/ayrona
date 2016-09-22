package com.ayronasystems.core.edr;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.concurrent.QueueRunnable;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.EdrModel;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class EdrLogger implements QueueRunnable<EdrRecord>{

    private long edrCount;

    private static EdrLogger edrLogger = new EdrLogger();

    private Dao dao = Singletons.INSTANCE.getDao();

    private EdrLogger(){
    }

    public static EdrLogger getLogger(){
        return edrLogger;
    }

    public void save(EdrRecord edrRecord){
        EdrModel edrModel = EdrModel.valueOf(edrRecord);
        dao.createEdr(edrModel);
        edrCount++;
    }

    public void success(EdrRecord edrRecord){
        edrRecord.status = EdrStatus.SUCCESSFUL;
        save(edrRecord);
    }

    public void error(EdrRecord edrRecord){
        edrRecord.status = EdrStatus.FAILED;
        save(edrRecord);
    }

    public void process(EdrRecord element) {
        save(element);
    }

    public long getEdrCount() {
        return edrCount;
    }
}
