package com.ayronasystems.core.dao.mysql.result;

/**
 * Created by gorkemgok on 01/02/16.
 */
public class DuplicateEntry extends AbstractDaoResult{

    public static final DaoResult VALUE = new DuplicateEntry();

    public DuplicateEntry (String message) {
        super (message);
    }

    private DuplicateEntry(){
        super("");
    }

    public DaoResultType getType () {
        return DaoResultType.DUPLICATE_ENTRY;
    }

}
