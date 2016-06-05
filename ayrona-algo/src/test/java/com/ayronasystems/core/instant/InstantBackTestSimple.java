package com.ayronasystems.core.instant;

import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.BackTestService;
import com.ayronasystems.core.service.StandaloneBackTestService;

/**
 * Created by gorkemgok on 01/06/16.
 */
public class InstantBackTestSimple {

    public static void main(String[] args) throws PrerequisiteException {
        FunctionFactory.scanFunctions ();
        String code =
                "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                "Sistem.SELL = Sistem.LT(SMA_5, SMA_20);";
        BackTestService bts = new StandaloneBackTestService ();
        BackTestResult result = bts.doBackTest (code, Symbol.VOB30, Period.M5, null, null);
        BackTestResult result2 = bts.doSimulationBackTest (code, Symbol.VOB30, Period.M5, null, null);

        System.out.println (result.getResult (MetricType.NET_PROFIT).getValue ());
        System.out.println (result2.getResult (MetricType.NET_PROFIT).getValue ());

        System.out.println (result);
        System.out.println (result2);


    }
}
