package com.ayronasystems.core.backtest;

import com.ayronasystems.core.strategy.Initiator;

/**
 * Created by gorkemgok on 18/05/16.
 */
public class BackTestInitiator implements Initiator {

    public static final Initiator INSTANCE = new BackTestInitiator();

    private static final String ID = "BTI";

    private BackTestInitiator () {
    }

    public String getIdentifier () {
        return ID;
    }

    public boolean isSameInitiator (Initiator initiator) {
        return initiator.getIdentifier ().equals (ID);
    }
}
