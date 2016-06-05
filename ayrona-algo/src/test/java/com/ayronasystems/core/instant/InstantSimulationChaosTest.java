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
 * Created by gorkemgok on 31/05/16.
 */
public class InstantSimulationChaosTest {

    public static void main(String[] args) throws PrerequisiteException {
        FunctionFactory.scanFunctions ();
        BackTestService bts = new StandaloneBackTestService ();
        for (int i = 1000;i < 1010; i++) {
            String par = i == 0 ? "" : ","+i;
            String code =
                    "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                            "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                            "var SAR = Sistem.SAR(0.02, 0.2"+par+");" +
                            "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                            "Sistem.SELL = Sistem.LT(Sistem.C, SAR);";

            BackTestResult result = bts.doSimulationBackTest (code, Symbol.VOB30, Period.M5, null, null);
            System.out.println (i+") "+result.getResult (MetricType.NET_PROFIT)
                                      .getValue ());
            //System.out.println (result);
        }

    }

}
