package com.ayronasystems.core.instant;

import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.BackTestService;
import com.ayronasystems.core.service.StandaloneBackTestService;

/**
 * Created by gorkemgok on 29/05/16.
 */
public class InstantSimulationTest {

    public static void main(String[] args) throws PrerequisiteException {
        FunctionFactory.scanFunctions ();
        BackTestService bts = new StandaloneBackTestService();
        String code =
                        "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                        "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                        "var SAR = Sistem.SAR(0.02, 0.2);" +
                        "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                        "Sistem.SELL = Sistem.LT(Sistem.C, SAR);";

        String code2 =
                        "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                        "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                        "var SAR = Sistem.SAR(0.02, 0.2, 20);" +
                        "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                        "Sistem.SELL = Sistem.LT(Sistem.C, SAR);";
        String code3 =
                        "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                        "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                        "var SAR = Sistem.SAR(0.02, 0.2, 100);" +
                        "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                        "Sistem.SELL = Sistem.LT(Sistem.C, SAR);";
        long start = System.currentTimeMillis ();
        BackTestResult result = bts.doBackTest (code, Symbols.of("TEST"), Period.M5, null, null, false);
        BackTestResult result2 = bts.doSimulationBackTest (code, Symbols.of("TEST"), Period.M5, null, null);
        BackTestResult result3 = bts.doSimulationBackTest (code2, Symbols.of("TEST"), Period.M5, null, null);
        BackTestResult result4 = bts.doSimulationBackTest (code3, Symbols.of("TEST"), Period.M5, null, null);
        long end = System.currentTimeMillis ();
        System.out.println ("Simulation BT lasts "+ (end-start)+"ms");

        System.out.println (result.getResult (MetricType.NET_PROFIT).getValue ());
        System.out.println (result2.getResult (MetricType.NET_PROFIT).getValue ());
        System.out.println (result3.getResult (MetricType.NET_PROFIT).getValue ());
        System.out.println (result4.getResult (MetricType.NET_PROFIT).getValue ());

        System.out.println (result);
        System.out.println (result2);
        System.out.println (result3);
        System.out.println (result4);

    }
}
