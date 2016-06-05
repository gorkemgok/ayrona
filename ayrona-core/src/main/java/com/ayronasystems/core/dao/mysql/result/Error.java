package com.ayronasystems.core.dao.mysql.result;

/**
 * Created by gorkemgok on 01/02/16.
 */
public class Error extends AbstractDaoResult {

    public static final DaoResult VALUE = new Error ();

    public Error (String message) {
        super (message);
    }

    public Error (Exception ex) {
        super (ex.getMessage ());
    }

    private Error(){
        super("");
    }

    public DaoResultType getType () {
        return DaoResultType.ERROR;
    }
}
