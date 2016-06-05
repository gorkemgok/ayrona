package com.ayronasystems.core.dao.mysql.result;

/**
 * Created by gorkemgok on 01/02/16.
 */
public class InvalidValues extends AbstractDaoResult {

    public static final DaoResult VALUE = new InvalidValues ();

    public InvalidValues (String message) {
        super (message);
    }

    private InvalidValues () {
        super ("");
    }

    public DaoResultType getType () {
        return DaoResultType.INVALID_VALUES;
    }
}
