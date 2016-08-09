package com.ayronasystems.core.edr;

/**
 * Created by gorkemg on 10.06.2016.
 */
public enum
EdrType {
    NEW_SIGNAL (EdrModule.STRATEGY),
    BOUND_ACCOUNT( EdrModule.STRATEGY ),
    UNBOUND_ACCOUNT( EdrModule.STRATEGY ),
    START_STRATEGY(EdrModule.STRATEGY),
    STOP_STRATEGY(EdrModule.STRATEGY),
    START_ACCOUNT(EdrModule.ACCOUNT),
    STOP_ACCOUNT(EdrModule.ACCOUNT),
    NEW_POSITION(EdrModule.ACCOUNT)
    ;

    private EdrModule edrModule;

    EdrType(EdrModule edrModule) {
        this.edrModule = edrModule;
    }

    public EdrModule getEdrModule() {
        return edrModule;
    }
}
