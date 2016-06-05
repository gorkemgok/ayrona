package com.ayronasystems.core.dao.mysql.result;

/**
 * Created by gorkemgok on 01/02/16.
 */
public class GeneralDaoResult extends AbstractDaoResult{

    private DaoResultType type;

    public GeneralDaoResult (DaoResultType type, String message) {
        super (message);
        this.type = type;
    }

    public GeneralDaoResult (DaoResultType type) {
        super (null);
        this.type = type;
    }

    public DaoResultType getType () {
        return type;
    }
}
