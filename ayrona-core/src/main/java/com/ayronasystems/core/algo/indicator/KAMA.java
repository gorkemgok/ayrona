package com.ayronasystems.core.algo.indicator;

import com.ayronasystems.core.algo.AbstractPeriodicFunction;
import com.ayronasystems.core.algo.Fn;
import com.ayronasystems.core.algo.FIOExchange;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * Created by gorkemgok on 19/03/16.
 */
@Fn(name = "KAMA")
public class KAMA extends AbstractPeriodicFunction {

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] output = new double[getOutputCount (inputs, params)];
        MInteger outNBElement = new MInteger();
        MInteger outBegIdx = new MInteger ();
        Core core = new Core();
        core.kama (0, inputs.getDataSize (0) - 1, inputs.getData (0), (int)params[0], outBegIdx, outNBElement, output);
        return new FIOExchange (output);
    }
}
