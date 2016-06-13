package com.ayronasystems.rest.bean;

/**
 * Created by gorkemgok on 13/06/16.
 */
public class PrerequisiteBean {

    private String field;

    private String check;

    public PrerequisiteBean () {
    }

    public PrerequisiteBean (String field, String check) {
        this.field = field;
        this.check = check;
    }

    public String getField () {
        return field;
    }

    public void setField (String field) {
        this.field = field;
    }

    public String getCheck () {
        return check;
    }

    public void setCheck (String check) {
        this.check = check;
    }
}
