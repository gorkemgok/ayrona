package com.ayronasystems.core.algo.function;

import com.ayronasystems.core.algo.AbstractNonPeriodicFunction;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Fn;

/**
 * Created by gorkemgok on 27/07/16.
 */
@Fn (name = "BARSINCE", inputCount = 1, paramCount = 0)
public class BARSINCE extends AbstractNonPeriodicFunction{

    public FIOExchange calculate (FIOExchange inputs, double... params) {
        double[] conditionArr = inputs.getData (0);
        double[] barsinceArr = new double[conditionArr.length];
        int barsince = 0;
        for ( int i = 0; i < conditionArr.length; i++ ) {
            if (conditionArr[i] == 1){
                barsince = 1;
                barsinceArr[i] = barsince;
            }else{
                if (barsince != 0){
                    barsince++;
                }
                barsinceArr[i] = barsince;
            }
        }
        return new FIOExchange (barsinceArr);
    }
}
