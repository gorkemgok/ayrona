package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Entity;

/**
 * Created by gorkemgok on 09/09/16.
 */
@Entity("optimizer_session")
public class OptimizerSessionModel extends TrainingSessionModel{

    private String code;

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    @Override
    public boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass () != o.getClass () ) {
            return false;
        }
        if ( !super.equals (o) ) {
            return false;
        }

        OptimizerSessionModel that = (OptimizerSessionModel) o;

        return code != null ? code.equals (that.code) : that.code == null;

    }

    @Override
    public int hashCode () {
        int result = super.hashCode ();
        result = 31 * result + (code != null ? code.hashCode () : 0);
        return result;
    }
}
