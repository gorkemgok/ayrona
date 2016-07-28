package com.ayronasystems.core.algo;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class OptimizerDef {

    private double start;

    private double end;

    private double step;

    public OptimizerDef (double start, double end, double step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    public double getStart () {
        return start;
    }

    public double getEnd () {
        return end;
    }

    public double getStep () {
        return step;
    }
}
