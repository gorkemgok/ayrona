package com.ayronasystems.core.dao.model;

import com.ayronasystems.core.edr.Edr;
import com.ayronasystems.core.edr.EdrModule;
import com.ayronasystems.core.edr.EdrStatus;
import com.ayronasystems.core.edr.EdrType;
import org.mongodb.morphia.annotations.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemg on 10.06.2016.
 */
@Entity("edr")
public class EdrModel extends BaseModel{

    private EdrModule module;

    private EdrType type;

    private EdrStatus status;

    private Map<String, String> properties = new HashMap<String, String>();

    public EdrModule getModule() {
        return module;
    }

    public EdrType getType() {
        return type;
    }

    public EdrStatus getStatus() {
        return status;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public static EdrModel valueOf(Edr edr){
        EdrModel edrModel = new EdrModel();
        edrModel.module = edr.getType().getEdrModule();
        edrModel.type= edr.getType();
        edrModel.status = edr.getStatus();
        edrModel.properties = edr.getProperties();
        return edrModel;
    }
}
