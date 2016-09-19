package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.backtest.BackTestCalculator;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.PeriodicBackTestCalculator;
import com.ayronasystems.core.backtest.PositionGenerator;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.genetics.core.FitnessFunction;

import java.util.List;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class OptimizerFitnessFunction implements FitnessFunction<OptimizerChromosome> {

    private MarketData ohlc;

    private String code;

    private ScoreCalculator scoreCalculator;

    public OptimizerFitnessFunction (String code, MarketData ohlc, ScoreCalculator scoreCalculator) {
        this.code = code;
        this.ohlc = ohlc;
        this.scoreCalculator = scoreCalculator;
    }

    public double calculateFitness (OptimizerChromosome chromosome) {
        Algo algo = Algo.createInstance (code, chromosome.getGen (), "");
        PositionGenerator positionGenerator = new PositionGenerator (algo);
        BackTestCalculator calculator = new PeriodicBackTestCalculator ();
        List<Position> positionList = positionGenerator.generate (ohlc);
        BackTestResult result = calculator.calculate (positionList, ohlc);
        double score = scoreCalculator.calculate (result);
        return score;
    }
}
