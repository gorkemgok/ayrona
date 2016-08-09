package com.ayronasystems.core.definition;

/**
 * Created by gorkemg on 09.06.2016.
 */
public enum TradeOperationResult {
    FAILED(true),
    SUCCESSFUL(false),
    NOT_ENOUGH_MONET(true),
    INVALID_PARAMETER(true),
    UNKNOWN(true);

    boolean failed;

    TradeOperationResult (boolean failed) {
        this.failed = failed;
    }

    public boolean isFailed () {
        return failed;
    }
}
