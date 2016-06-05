package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractNonPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;

/**
 * Created by gorkemgok on 13/05/16.
 */
@Fn(name = "GT", paramCount = 0, sameSizeInput = false)
public class GT extends AbstractNonPeriodicFunction {

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] output = new double[getOutputCount (inputs, params)];
        if (inputs.getDataCount () > 1) {
            double[] in1 = inputs.getData (0);
            double[] in2 = inputs.getData (1);
            boolean isIn1Short = in1.length < in2.length;
            int diff = Math.abs (in1.length - in2.length);
            for ( int i = 0; i < output.length; i++ ) {
                if ( isIn1Short ) {
                    output[i] = in1[i] > in2[i + diff] ? 1 : 0;
                } else {
                    output[i] = in1[i + diff] > in2[i] ? 1 : 0;
                }

            }
        }else{
            double[] in = inputs.getData (0);
            for ( int i = 0; i < output.length; i++ ) {
                output[i] = in[i] > params[0] ? 1 : 0;
            }
        }
        return new FIOExchange (output);
    }
}
