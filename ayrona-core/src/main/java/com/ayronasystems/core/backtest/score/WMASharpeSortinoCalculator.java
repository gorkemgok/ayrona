package com.ayronasystems.core.backtest.score;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.ResultPeriod;

import java.util.Date;
import java.util.Map;

/**
 * Created by gorkemgok on 31/08/16.
 */
public class WMASharpeSortinoCalculator implements ScoreCalculator<Double>{

    private ResultPeriod resultPeriod;

    private SharpeSortinoType type;

    private PeriodicSharpeSortinoCalculator sharpeSortinoCalculator;

    public WMASharpeSortinoCalculator (ResultPeriod resultPeriod, SharpeSortinoType type) {
        this.resultPeriod = resultPeriod;
        this.type = type;
        sharpeSortinoCalculator = new PeriodicSharpeSortinoCalculator (resultPeriod, type);
    }

    public Double calculate (BackTestResult btr) {
        Map<Date, Double> ssMap = sharpeSortinoCalculator.calculate (btr);
        double wTotal = 0;
        int divider = 0;
        int i = 1;
        for(Map.Entry<Date, Double> entry : ssMap.entrySet ()){
            wTotal += entry.getValue () * i;
            divider += i;
            i++;
        }
        return wTotal / divider;
    }
}
