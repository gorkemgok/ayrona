package com.ayronasystems.core.backtest;

import com.ayronasystems.core.util.date.*;

import java.util.*;

import static com.ayronasystems.core.backtest.ResultQuantaMetric.*;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class PeriodicAggregator {

    private ResultPeriod resultPeriod;

    private DatePeriodComparator comparator;

    private List<ResultQuanta> resultList = new ArrayList<ResultQuanta> ();

    public PeriodicAggregator (ResultPeriod resultPeriod) {
        this.resultPeriod = resultPeriod;
        this.comparator = resultPeriod.generateComparator ();
    }

    private Date   periodDate;
    private double equity, netProfit, win, winCount, loss, lossCount, minMdd;
    private double equityPercentage, netProfitPercentage,
            winPercentage, lossPercentage, minMddPercentage;

    public void forwardDate(Date date){
        if ( periodDate == null){
            periodDate = date;
        }
        if (!comparator.isSamePeriod (periodDate, date)){
            endPeriod ();
            periodDate = date;
        }
    }

    public void endPeriod(){
        equity += netProfit;
        equityPercentage += netProfitPercentage;
        ResultQuanta resultQuanta = generateResultQuanta ();
        resultList.add (resultQuanta);
        resetCurrents ();
    }

    public void addProfit(double profit, double profitPercentage){
        netProfit += profit;
        netProfitPercentage += profitPercentage;
        if ( profit > 0 ){
            win += profit;
            winPercentage += profitPercentage;
            winCount++;
        }else if (profit < 0){
            loss += profit;
            lossPercentage += profitPercentage;
            lossCount++;
        }else{

        }
    }

    public void addMdd(double mdd, double mddPercentage){
        minMdd = Math.min (minMdd, mdd);
        minMddPercentage = Math.min (minMddPercentage, mddPercentage);
    }

    private void resetCurrents(){
        netProfit = 0;
        minMdd = Double.MAX_VALUE;
        minMddPercentage = Double.MAX_VALUE;
        netProfit = 0; win = 0; winCount = 0; loss = 0; lossCount = 0; minMdd = 0;
        netProfitPercentage = 0; winPercentage = 0; lossPercentage = 0; minMddPercentage = 0;
    }

    private ResultQuanta generateResultQuanta(){
        Map<ResultQuantaMetric, Double> valueList = new HashMap<ResultQuantaMetric, Double> ();
        valueList.put (EQUITY, equity);
        valueList.put (EQUITY_PERCENTAGE, equityPercentage);
        valueList.put (NET_PROFIT, netProfit);
        valueList.put (NET_PROFIT_PERCENTAGE, netProfitPercentage);
        valueList.put (GROSS_PROFIT, win);
        valueList.put (GROSS_PROFIT_PERCENTAGE, winPercentage);
        valueList.put (GROSS_LOSS, loss);
        valueList.put (GROSS_LOSS_PERCENTAGE, lossPercentage);
        valueList.put (PROFIT_FACTOR, loss != 0 ? Math.abs (win / loss) : win);

        valueList.put (MDD, minMdd);
        valueList.put (MDD_PERCENTAGE, minMddPercentage);

        double tradeCount = winCount + lossCount;
        valueList.put (TOTAL_NUMBER_OF_TRADES, winCount + lossCount);
        valueList.put (WINNING_TRADE_COUNT, winCount);
        valueList.put (LOSING_TRADE_COUNT, lossCount);
        valueList.put (PROFITABLE_PERCENT, lossCount > 0 ? 100 * (winCount / lossCount) : winCount);

        valueList.put (AVE_NET_PROFIT, netProfit / tradeCount);
        valueList.put (AVE_NET_PROFIT_PERCENT, netProfitPercentage / tradeCount);
        double awt = win / winCount;
        valueList.put (AVE_WINNING_TRADE, awt);
        double awtp = winPercentage / winCount;
        valueList.put (AVE_WINNING_TRADE_PERCENT, awtp);
        double alt = loss / lossCount;
        valueList.put (AVE_LOSING_TRADE, alt);
        double altp = lossPercentage / lossCount;
        valueList.put (AVE_LOSING_TRADE_PERCENT, altp);
        valueList.put (RATIO_AVE_WINNING_LOSING, awt / alt);
        valueList.put (RATIO_AVE_WINNING_LOSING_PERCENT, awtp / altp);

        return new ResultQuanta (periodDate, valueList);
    }

    public ResultPeriod getResultPeriod () {
        return resultPeriod;
    }

    public List<ResultQuanta> getResultList () {
        return resultList;
    }

    public void clear(){
        periodDate = null;
        equity = 0;
        equityPercentage = 0;
        resetCurrents ();
        resultList.clear ();
    }

}
