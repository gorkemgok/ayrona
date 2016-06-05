package com.ayronasystems.core.dao.mysql.result;

/**
 * Created by gorkemgok on 01/02/16.
 */
public interface DaoResult {

    DaoResultType getType ();

    String getMessage ();

    boolean is (DaoResultType type);
}
