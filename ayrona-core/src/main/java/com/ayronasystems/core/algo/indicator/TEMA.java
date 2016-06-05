package com.ayronasystems.core.algo.indicator;

import com.ayronasystems.core.algo.AbstractPeriodicFunction;
import com.ayronasystems.core.algo.Fn;
import com.ayronasystems.core.algo.FIOExchange;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * Created by gorkemgok on 19/03/16.
 */
@Fn(name = "TEMA")
public class TEMA extends AbstractPeriodicFunction {

    @Override
    public int getNeededInputCount (double... params) {
        return super.getNeededInputCount (params) * 3 - 1;
    }

    @Override
    public int getOutputCount (FIOExchange inputs, double... params) {
        return super.getOutputCount (inputs, params) + 1;
    }

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] output = new double[getOutputCount (inputs, params)];
        MInteger outNBElement = new MInteger();
        MInteger outBegIdx = new MInteger ();
        Core core = new Core();
        core.tema (0, inputs.getDataSize (0) - 1, inputs.getData (0), (int)params[0], outBegIdx, outNBElement, output);
        return new FIOExchange (output);
    }
}
