package com.ayronasystems.core.backtest;

/**
 * Created by gorkemgok on 06/01/16.
 */
public class MetricValue<T> {

    private T value;

    public MetricValue (T value) {
        this.value = value;
    }

    public T getValue () {
        return value;
    }

    public void setValue (T value) {
        this.value = value;
    }
}
