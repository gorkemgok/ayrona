package com.ayronasystems.core.definition;

/**
 * Created by gorkemgok on 12/05/16.
 */
public enum Signal {
    BUY(0),
    SELL(1),
    OPEN_BUY(2),
    CLOSE_BUY(3),
    OPEN_SELL(4),
    CLOSE_SELL(5),
    HOLD(6),
    AMBIGUOUS(7);

    private int signal;

    Signal (int signal) {
        this.signal = signal;
    }

    public int getSignal () {
        return signal;
    }
}
