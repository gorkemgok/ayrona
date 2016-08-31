package com.ayronasystems.core.backtest.score;

import com.ayronasystems.core.backtest.BackTestResult;

/**
 * Created by gorkemgok on 31/08/16.
 */
public interface ScoreCalculator<Output> {

    Output calculate(BackTestResult btr);

}
