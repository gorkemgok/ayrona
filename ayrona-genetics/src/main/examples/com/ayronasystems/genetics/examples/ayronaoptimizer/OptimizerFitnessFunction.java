package com.ayronasystems.genetics.examples.ayronaoptimizer;

import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.backtest.*;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.genetics.core.FitnessFunction;

import java.text.DecimalFormat;
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
        DecimalFormat df = new DecimalFormat ("##.###");
        Algo algo = Algo.createInstance (code, chromosome.getGen (), "");
        PositionGenerator positionGenerator = new PositionGenerator (algo);
        BackTestCalculator calculator = new PeriodicBackTestCalculator ();
        List<Position> positionList = positionGenerator.generate (ohlc);
        BackTestResult result = calculator.calculate (positionList, ohlc);
        double score = scoreCalculator.calculate (result);
        List<ResultQuanta> resultQuantaList = result.getPeriodicResultMap ().get (ResultPeriod.INFINITY);
        ResultQuanta ooResult = resultQuantaList.get (0);
        double profit = ooResult.getValue (ResultQuantaMetric.NET_PROFIT);
        double positionCount = ooResult.getValue (ResultQuantaMetric.TOTAL_NUMBER_OF_TRADES);
        //System.out.println (df.format (score)+":( "+df.format (profit)+", "+df.format (positionCount)+" )"+chromosome);
        return score;
    }
}
