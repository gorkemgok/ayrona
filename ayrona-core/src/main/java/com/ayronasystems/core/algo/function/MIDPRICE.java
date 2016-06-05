package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * Created by gorkemgok on 19/03/16.
 */
@Fn(name = "MIDPRICE", inputCount = 2)
public class MIDPRICE extends AbstractPeriodicFunction {

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] output = new double[getOutputCount (inputs, params)];
        MInteger outNBElement = new MInteger();
        MInteger outBegIdx = new MInteger ();
        Core core = new Core();
        core.midPrice (0, inputs.getDataSize (0) - 1,
                       inputs.getData (0), //High
                       inputs.getData (1), //Low
                       (int)params[0], outBegIdx, outNBElement, output);
        return new FIOExchange (output);
    }
}
