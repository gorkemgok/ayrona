package com.ayronasystems.core.instant;

import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.AlgoCreator;
import com.ayronasystems.core.algo.FunctionFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by gorkemgok on 25/05/16.
 */
public class InstantRhinoTest {

    public static void main(String[] args){
        FunctionFactory.scanFunctions ();
        AlgoCreator algoCreator = new AlgoCreator ();
        Context context = Context.enter ();
        Scriptable scope = context.initStandardObjects ();
        Object jsAlgoCreator = Context.javaToJS (algoCreator, scope);
        ScriptableObject.putProperty (scope, "Sistem", jsAlgoCreator);
        Object result = context.evaluateString(scope,
                                               "var SMA_1 = Sistem.SMA(Sistem.C,2);" +
                                               "var SMA_2 = Sistem.SMA(SMA_1, 2);" +
                                               "Sistem.BUY = Sistem.AND(Sistem.LT(SMA_1, SMA_2), Sistem.GT(SMA_1, SMA_2));" +
                                               "Sistem.SELL = Sistem.AND(Sistem.GT(SMA_1, SMA_2), Sistem.GT(SMA_1, SMA_2));"
                , "<cmd>", 1, null);
        Algo algo = new Algo (algoCreator.NAME, algoCreator.BUY, algoCreator.SELL);
        System.out.println (result);
        System.out.println (algoCreator.BUY.toString ());
        System.out.println (algoCreator.SELL.toString ());
        Context.exit ();
    }

}
