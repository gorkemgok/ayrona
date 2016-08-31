package com.ayronasystems.core.backtest.score;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.ResultPeriod;
import com.ayronasystems.core.backtest.ResultQuanta;
import com.ayronasystems.core.backtest.ResultQuantaMetric;
import com.ayronasystems.core.util.date.DatePeriodComparator;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.*;

/**
 * Created by gorkemgok on 31/08/16.
 */
public class PeriodicSharpeSortinoCalculator implements ScoreCalculator<Map<Date, Double>>{

    private SharpeSortinoType type;

    private ResultPeriod resultPeriod;

    private boolean isSharpe;

    public PeriodicSharpeSortinoCalculator (ResultPeriod resultPeriod, SharpeSortinoType type) {
        this.resultPeriod = resultPeriod;
        this.type = type;
        isSharpe = type == SharpeSortinoType.SHARPE;
    }

    public Map<Date, Double> calculate (BackTestResult btr) {
        Map<ResultPeriod, List<ResultQuanta>> prm = btr.getPeriodicResultMap ();
        List<ResultQuanta> subResultQuantaList = prm.get (resultPeriod.getSubPeriod ());
        List<ResultQuanta> resultQuantaList = prm.get (resultPeriod);
        DatePeriodComparator comparator = resultPeriod.generateComparator ();

        Date currentDate = null;
        List<Double> profits = null;
        Map<Date, List<Double>> profitsMap = new TreeMap<Date, List<Double>> ();
        for (ResultQuanta resultQuanta : subResultQuantaList){
            Date date = resultQuanta.getDate ();
            double profit = resultQuanta.getValue (ResultQuantaMetric.NET_PROFIT_PERCENTAGE);
            if ( !comparator.isSamePeriod (currentDate, date) ){
                if (profits != null ){
                    profitsMap.put (currentDate, profits);
                }
                currentDate = date;
                profits = new ArrayList<Double> ();
            }
            if (isSharpe || profit < 0) {
                profits.add (profit);
            }
        }
        if (profits != null ){
            profitsMap.put (currentDate, profits);
        }
        Map<Date, Double> sharpeMap = new TreeMap<Date, Double> ();
        for (Map.Entry<Date, List<Double>> entry : profitsMap.entrySet ()){
            double[] values = new double[entry.getValue ().size ()];
            int i = 0;
            for (Double d : entry.getValue ()){
                values[i] = d;
                i++;
            }
            StandardDeviation std = new StandardDeviation ();
            double stdValue = std.evaluate (values);
            Date stdDate = entry.getKey ();
            for (ResultQuanta resultQuanta : resultQuantaList){
                Date date = resultQuanta.getDate ();
                if (comparator.isSamePeriod (stdDate, date)){
                    double netProfit = resultQuanta.getValue (ResultQuantaMetric.NET_PROFIT_PERCENTAGE);
                    double tradeCount = resultQuanta.getValue (ResultQuantaMetric.TOTAL_NUMBER_OF_TRADES);
                    double sharpe = (netProfit / tradeCount) / (stdValue != 0 && !Double.isNaN (stdValue) ? stdValue : 1);
                    sharpeMap.put (stdDate, sharpe);
                    break;
                }
            }
        }
        return sharpeMap;
    }
}
