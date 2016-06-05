package com.ayronasystems.core.definition;

/**
 * Created by gorkemgok on 20/03/16.
 */
public enum PriceColumn {
    OPEN(0),
    HIGH(1),
    LOW(2),
    CLOSE(3),
    ASK(4),
    MID(5),
    BID(6),
    VOLUME(7);

    private int index;

    PriceColumn (int index) {
        this.index = index;
    }

    public int getIndex () {
        return index;
    }
}
