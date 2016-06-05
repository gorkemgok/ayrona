package com.ayronasystems.core.algo;

/**
 * Created by gorkemgok on 19/03/16.
 */
public abstract class AbstractPeriodicFunction extends AbstractFunction{

    public int  getNeededInputCount (double... params) {
        int p = (int)params[0];
        int d = getDefinition ().getDepth ();
        int bo = getDefinition ().getBegOffset ();
        return  p * d - (bo * d - 1);
    }

    public int getOutputCount (FIOExchange inputs, double... params) {
        int is = inputs.getShortestDataSize ();
        int n = getNeededInputCount (params);
        return is - n + 1;//TODO : do minus count control
    }

}
