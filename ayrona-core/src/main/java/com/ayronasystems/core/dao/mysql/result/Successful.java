package com.ayronasystems.core.dao.mysql.result;

/**
 * Created by gorkemgok on 01/02/16.
 */
public class Successful extends AbstractDaoResult{

    public static final DaoResult VALUE = new Successful();

    public Successful (String message) {
        super (message);
    }

    private Successful () {
        super ("");
    }

    public DaoResultType getType () {
        return DaoResultType.SUCCESSFUL;
    }
}
