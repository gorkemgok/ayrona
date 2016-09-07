package com.ayronasystems.genetics.examples.ayronaoptimizer;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.ResultPeriod;
import com.ayronasystems.core.backtest.score.SharpeSortinoType;
import com.ayronasystems.core.backtest.score.WMASharpeSortinoCalculator;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class ScoreCalculator {

    private WMASharpeSortinoCalculator calc = new WMASharpeSortinoCalculator (ResultPeriod.YEAR, SharpeSortinoType.SORTINO);

    public double calculate(BackTestResult result){
        return calc.calculate (result);
        //return result.getPeriodicResultMap ().get (ResultPeriod.INFINITY).get (0).getValue (ResultQuantaMetric.NET_PROFIT_PERCENTAGE);
    }

}
