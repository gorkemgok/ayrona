package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by gorkemgok on 05/10/2016.
 */
public class ScoreCalculatorTest {

    @Test
    public void calculate () throws Exception {
        String scoreEquation = "np/pf";
        ScoreCalculator scoreCalculator = new ScoreCalculator (scoreEquation);
        BackTestResult btr = new BackTestResult (new Date (0), new Date(1));
        btr.setResult (MetricType.NET_PROFIT, 15);
        btr.setResult (MetricType.PROFIT_FACTOR, 0.2);

        double actual = scoreCalculator.calculate (btr);

        Assert.assertEquals (75, actual, 0);
    }

}