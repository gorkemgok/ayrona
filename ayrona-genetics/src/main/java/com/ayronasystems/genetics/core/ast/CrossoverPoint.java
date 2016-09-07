package com.ayronasystems.genetics.core.ast;

/**
 * Created by gorkemgok on 02/04/16.
 */
public class CrossoverPoint<N extends Node> {

    private final int pointIndex;

    private final N node;

    public CrossoverPoint (int pointIndex, N node) {
        this.pointIndex = pointIndex;
        this.node = node;
    }

    public int getPointIndex () {
        return pointIndex;
    }

    public N getNode () {
        return node;
    }
}
