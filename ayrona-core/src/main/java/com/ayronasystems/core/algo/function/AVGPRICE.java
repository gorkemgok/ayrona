package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractNonPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * Created by gorkemgok on 19/03/16.
 */
@Fn(name = "AVGPRICE", inputCount = 4, paramCount = 0)
public class AVGPRICE extends AbstractNonPeriodicFunction {

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] output = new double[getOutputCount (inputs, params)];
        MInteger outNBElement = new MInteger();
        MInteger outBegIdx = new MInteger ();
        Core core = new Core();
        core.avgPrice (0, inputs.getDataSize (0) - 1,
                       inputs.getData (0), //Open
                       inputs.getData (1), //High
                       inputs.getData (2), //Low
                       inputs.getData (3), //Close
                       outBegIdx, outNBElement, output);
        return new FIOExchange (output);
    }
}
