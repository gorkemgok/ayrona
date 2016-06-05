package com.ayronasystems.core.algo;


/**
 * Created by gorkemgok on 20/03/16.
 */
public abstract class AbstractNonPeriodicFunction extends AbstractFunction{

    public int getNeededInputCount (double... params) {
        return 1;
    }

    public int getOutputCount (FIOExchange inputs, double... params) {
        return inputs.getShortestDataSize ();
    }

}
