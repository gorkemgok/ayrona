package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Entity("strategy")
public class StrategyModel extends BaseModel {

    private String name;

    private String code;

    private List<String> boundAccountIds;

    public List<String> getBoundAccountIds () {
        return boundAccountIds;
    }

    public void setBoundAccountIds (List<String> boundAccountIds) {
        this.boundAccountIds = boundAccountIds;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }}
