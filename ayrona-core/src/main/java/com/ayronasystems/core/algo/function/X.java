package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;

/**
 * Created by gorkemgok on 30/05/16.
 */
@Fn (name = "X")
public class X extends AbstractPeriodicFunction{

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        int size = (int)params[0];
        double[] output = new double[size];
        double[] input = inputs.getData (0);
        for ( int i = 0; i < size; i++ ) {
            output[i] = input[i + (input.length - size)];
        }
        return new FIOExchange (output);
    }

    @Override
    public int getOutputCount (FIOExchange inputs, double... params) {
        return (int)params[0];
    }
}
