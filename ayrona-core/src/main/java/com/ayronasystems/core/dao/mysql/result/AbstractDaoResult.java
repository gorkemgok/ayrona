package com.ayronasystems.core.dao.mysql.result;

/**
 * Created by gorkemgok on 01/02/16.
 */
public abstract class AbstractDaoResult implements DaoResult{

    protected String message;

    public AbstractDaoResult (String message) {
        this.message = message;
    }

    public String getMessage () {
        return message;
    }

    public boolean is (DaoResultType type) {
        return getType ().equals (type);
    }
}
