package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractNonPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * Created by gorkemgok on 13/05/16.
 */
@Fn(name="MULT", inputCount = 2, paramCount = 0)
public class MULT extends AbstractNonPeriodicFunction {

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] input1 = inputs.getData (0);
        double[] input2 = inputs.getData (1);
        double[] output = new double[input1.length];
        MInteger outNBElement = new MInteger();
        MInteger outBegIdx = new MInteger ();
        Core core = new Core();
        core.mult (0, input1.length - 1,
                  input1, input2,
                  outBegIdx, outNBElement, output);

        return new FIOExchange (output);
    }
}
