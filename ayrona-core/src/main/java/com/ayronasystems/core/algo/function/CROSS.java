package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;

/**
 * Created by gorkemgok on 05/02/15.
 */
@Fn( name = "CROSS", inputCount = 2, paramCount = 0)
public class CROSS extends AbstractFunction {

    public int getNeededInputCount (double... params) {
        return 2 ;
    }

    public int getOutputCount(FIOExchange inputs, double... params){
        return getNeededInputCount (params) - 1;
    }

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] input1 = inputs.getData (0);
        double[] input2 = inputs.getData (1);

        double[][] result = new double[1][input1.length - getOutputCount (inputs, params)];

        int currState = input1[0] > input2[0] ? 1 : input1[0] < input2[0] ? -1 : 0;
        for ( int i = 1; i < input1.length; i++ ) {
            int state = input1[i] > input2[i] ? 1 : input1[i] < input2[i] ? -1 : 0;
            result[0][i-1] = currState != state && state != 0 ? 1 : 0;
            currState = state != 0 ? state : currState;
        }
        return new FIOExchange (result);
    }
}
