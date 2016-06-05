package com.ayronasystems.core.algo.tree;

/**
 * Created by gorkemgok on 20/03/16.
 */
public enum Column {
    OPEN(0),
    HIGH(1),
    LOW(2),
    CLOSE(3);

    private int index;

    Column (int index) {
        this.index = index;
    }

    public int getIndex () {
        return index;
    }
}
