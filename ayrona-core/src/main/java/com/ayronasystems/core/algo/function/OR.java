package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractNonPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;

/**
 * Created by gorkemgok on 14/05/16.
 */
@Fn(name = "OR", inputCount = 2, paramCount = 0)
public class OR extends AbstractNonPeriodicFunction {

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] output = new double[getOutputCount (inputs, params)];
        double[] in1 = inputs.getData (0);
        double[] in2 = inputs.getData (1);
        boolean isIn1Short = in1.length < in2.length;
        int diff = Math.abs (in1.length - in2.length);
        for ( int i = 0; i < output.length; i++ ) {
            if ( isIn1Short ) {
                output[i] = in1[i] == 1 || in2[i + diff] == 1 ? 1 : 0;
            } else {
                output[i] = in1[i + diff] == 1 || in2[i] == 1 ? 1 : 0;
            }

        }
        return new FIOExchange (output);
    }
}
