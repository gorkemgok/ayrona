package com.ayronasystems.core.timeseries.type;

import java.util.Arrays;

/**
 * Created by gorkemgok on 21/06/15.
 */
public class Indicator {

    public static final int COLUMN_COUNT = 1;

    private final String name;

    private final double[] parameters;

    private double value;

    private boolean isCalculated;

    public Indicator(String name, double[] parameters , double value) {
        this.value = value;
        this.name = name;
        this.parameters = parameters;
        this.isCalculated = true;
    }

    public Indicator(String name, double[] parameters) {
        this.name = name;
        this.parameters = parameters;
        this.isCalculated = false;
    }

    public boolean isCalculated() {
        return isCalculated;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        isCalculated = true;
    }

    public String getName() {
        return name;
    }

    public double[] getParameters() {
        return parameters;
    }

    public boolean isSame(Indicator indicator){
        if (indicator.getName ().equals (this.getName ()) && Arrays.equals (indicator.getParameters (), this.getParameters ())){
            return true;
        }
        return false;
    }
}
