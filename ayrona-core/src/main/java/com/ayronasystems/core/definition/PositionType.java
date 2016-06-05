package com.ayronasystems.core.definition;

/**
 * Created by gorkemgok on 12/05/16.
 */
public enum PositionType {
    LONG(0),
    SHORT(1);

    private int type;

    PositionType (int type) {
        this.type = type;
    }

    public int getType () {
        return type;
    }
}
