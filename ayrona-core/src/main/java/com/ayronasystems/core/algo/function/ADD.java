package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractNonPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * Created by gorkemgok on 13/05/16.
 */
@Fn (name="ADD", inputCount = 2, paramCount = 0, sameSizeInput = false)
public class ADD extends AbstractNonPeriodicFunction {

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] input1 = inputs.getDataWithShortestSize (0);
        double[] input2 = inputs.getDataWithShortestSize (1);
        double[] output = new double[inputs.getShortestDataSize ()];
        MInteger outNBElement = new MInteger();
        MInteger outBegIdx = new MInteger ();
        Core core = new Core();
        int endIdx = Math.min (input1.length, input2.length) - 1;
        core.add (0, endIdx,
                       input1, input2,
                       outBegIdx, outNBElement, output);

        return new FIOExchange (output);
    }
}
