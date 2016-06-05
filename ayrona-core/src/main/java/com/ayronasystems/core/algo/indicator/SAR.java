package com.ayronasystems.core.algo.indicator;

import com.ayronasystems.core.algo.AbstractFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

/**
 * Created by gorkemgok on 13/05/16.
 */
@Fn (name="SAR", inputCount = 2, paramCount = 0)
public class SAR extends AbstractFunction{

    public int getNeededInputCount (double... params) {
        return 2 ;
    }

    public int getOutputCount(FIOExchange inputs, double... params){
        return inputs.getShortestDataSize () - 1;
    }

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] output = new double[getOutputCount (inputs, params)];
        Core core = new Core();
        MInteger outNBElement = new MInteger();
        MInteger outBegIdx = new MInteger ();
        core.sar (0,
                  inputs.getShortestDataSize () - 1,
                  inputs.getData (0),
                  inputs.getData (1),
                  params[0],
                  params[1],
                  outBegIdx, outNBElement,
                  output);
        return new FIOExchange (output);
    }
}
