package com.ayronasystems.core.backtest;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.timeseries.series.BasicTimeSeries;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.*;

/**
 * Created by gorkemgok on 19/05/16.
 */
public class BackTestCalculator {

    public BackTestResult calculate(List<Position> positionList, MarketData marketData){
        int i = 0;
        List<Position> openPositionList = new ArrayList<Position> ();
        TimeSeries<EquityBar> equitySeries = BasicTimeSeries.getDynamicSizeInstance (EquityBar.class);
        double mdd;
        double mddPercentage;
        double instantProfit;
        double instantProfitPercentage;
        double equity = 0;
        double grossProfit = 0, grossLoss = 0, maxWinning = 0, maxLosing = 0, winningBarCount = 0, losingBarCount = 0;
        double grossProfitPercentage = 0, grossLossPercentage = 0, maxWinningPercentage = 0, maxLosingPercentage = 0, winningBarCountPercentage = 0, losingBarCountPercentage = 0;
        int maxConsWinning = 0, maxConsLosing = 0, losingTradeCount = 0, winningTradeCount = 0;
        double tradeMDD = 0;
        int wcc = 0;
        int lcc = 0;
        int positionCount = positionList.size ();
        for ( Moment moment : marketData ){
            Date momentDate = moment.getDate ();
            double low = moment.get (ColumnDefinition.LOW);
            boolean fw = true;
            while ( fw && i < positionCount){
                Position position = positionList.get (i);
                if (momentDate.after (position.getIdealOpenDate ())){
                    openPositionList.add (position);
                    i++;
                }
                else{
                    fw = false;
                }
            }
            mdd = 0;
            instantProfit = 0;
            instantProfitPercentage = 0;
            Iterator<Position> iterator = openPositionList.iterator ();
            while ( iterator.hasNext ()){
                Position position = iterator.next ();
                if (position.isClosed () && (position.getIdealCloseDate ().equals (momentDate) || position.getIdealCloseDate ().before (momentDate))){
                    mdd = Math.min (mdd, position.getDirection () == Direction.LONG ? position.getOpenPrice () - low : low -position.getOpenPrice ());
                    double profit = position.calculateProfit ();
                    double profitPercentage = position.calculateProfit ();
                    if (profit > 0){
                        grossProfit += profit;
                        grossLossPercentage += profitPercentage;
                        winningTradeCount++;
                        maxWinning = Math.max (maxWinning, profit);
                        maxWinningPercentage = Math.max (maxWinningPercentage, profitPercentage);
                        wcc++;
                        if (lcc > 0){
                            maxConsLosing = Math.max (maxConsLosing, lcc);
                            lcc = 0;
                        }
                    }else if (profit < 0){
                        grossLoss += profit;
                        grossLossPercentage += profitPercentage;
                        losingTradeCount++;
                        maxLosing = Math.min (maxLosing, profit);
                        maxLosingPercentage = Math.min (maxLosingPercentage, profitPercentage);
                        lcc++;
                        if (wcc > 0){
                            maxConsWinning = Math.max (maxConsWinning, wcc);
                            wcc = 0;
                        }
                    }
                    instantProfit += profit;
                    instantProfitPercentage += profitPercentage;
                    iterator.remove ();
                }
            }
            if (equity > 0){
                winningBarCount++;
            }else if (equity < 0 ){
                losingBarCount++;
            }
            equity += instantProfit;
            tradeMDD = Math.min (tradeMDD, mdd);
            EquityBar equityBar = new EquityBar (momentDate, equity, mdd, instantProfit);
            equitySeries.addMoment (equityBar);
        }
        BackTestResult result = new BackTestResult (marketData.getBeginningDate (), marketData.getEndingDate ());
        result.setPositionList (positionList);
        result.setResult (MetricType.EQUITY_SERIES, new MetricValue (equitySeries));
        result.setResult (MetricType.MAX_TRADE_DRAWDOWN, tradeMDD);
        long start = System.currentTimeMillis ();
        result.setResult (MetricType.STABILITY, rSquared (equitySeries));
        long end = System.currentTimeMillis ();
        double std = std (equitySeries, false);
        double negativeStd = std (equitySeries, true);
        //System.out.println((end - start)+"ms");
        calculateDerivatives (result, grossProfit, grossProfitPercentage, grossLoss, grossLossPercentage, std, negativeStd, maxWinning, maxLosing, winningBarCount, losingBarCount, maxConsWinning, maxConsLosing, losingTradeCount, winningTradeCount);
        return result;
    }

    private double rSquared(TimeSeries<EquityBar> equitySeries){
        SimpleRegression regression = new SimpleRegression ();
        int i = 0;
        for(EquityBar equityBar : equitySeries){
            regression.addData (i++, equityBar.get (ColumnDefinition.EQUITY));
        }
        return regression.regress ().getRSquared ();
    }

    private double std(TimeSeries<EquityBar> equitySeries, boolean onlyLoss){
        StandardDeviation std = new StandardDeviation ();
        double[] values = new double[equitySeries.size ()];
        int j = 0;
        for(int i = 0; i < equitySeries.size (); i++){
            double ip = equitySeries.getMoment (i)
                                    .getInstantProfit ();
            if (ip != 0) {
                if ( !onlyLoss ) {
                    values[j] = ip;
                    j++;
                } else if ( ip < 0 ) {
                    values[j] = ip;
                    j++;
                }
            }
        }
        double[] valuesTrim = Arrays.copyOf (values, j);
        return std.evaluate (valuesTrim);
    }

    private static void calculateDerivatives (Summary result, double grossProfit, double grossProfitPercentage, double grossLoss, double grossLossPercentage, double std, double negativeStd, double maxWinning, double maxLosing, double winningBarCount, double losingBarCount, int maxConsWinning, int maxConsLosing, int losingTradeCount, int winningTradeCount) {
        int totalTradeCount = winningTradeCount + losingTradeCount;
        double netProfit = grossProfit + grossLoss;
        double netProfitPercentage = grossProfitPercentage + grossLossPercentage;
        result.setResult (MetricType.NET_PROFIT, new MetricValue<Double> (netProfit));
        result.setResult (MetricType.GROSS_PROFIT, new MetricValue<Double> (grossProfit));
        result.setResult (MetricType.GROSS_LOSS, new MetricValue<Double> (grossLoss));
        result.setResult (MetricType.NET_PROFIT_PERCENTAGE, new MetricValue<Double> (netProfitPercentage));
        result.setResult (MetricType.GROSS_PROFIT_PERCENTAGE, new MetricValue<Double> (grossProfitPercentage));
        result.setResult (MetricType.GROSS_LOSS_PERCENTAGE, new MetricValue<Double> (grossLossPercentage));
        result.setResult (MetricType.PROFIT_FACTOR, new MetricValue<Double> (grossLoss != 0 ? Math.abs(grossProfit / grossLoss) : 0));

        result.setResult (MetricType.PROFIT_STD, std);
        result.setResult (MetricType.NEGATIVE_PROFIT_STD, negativeStd);

        result.setResult (MetricType.TOTAL_NUMBER_OF_TRADES, new MetricValue<Integer>(totalTradeCount));
        result.setResult (MetricType.PROFITABLE_PERCENT, new MetricValue<Double> (losingTradeCount != 0 ? 100 * (double)winningTradeCount / (double)losingTradeCount : 0));
        result.setResult (MetricType.WINNING_TRADE_COUNT, new MetricValue<Integer>(winningTradeCount));
        result.setResult (MetricType.LOSING_TRADE_COUNT, new MetricValue<Integer>(losingTradeCount));

        result.setResult (MetricType.AVE_TRADE_NET_PROFIT, new MetricValue<Double> (totalTradeCount != 0 ? netProfit / totalTradeCount : 0));
        result.setResult (MetricType.AVE_WINNING_TRADE, new MetricValue<Double> (winningTradeCount != 0 ? grossProfit / winningTradeCount : 0));
        result.setResult (MetricType.AVE_LOSING_TRADE, new MetricValue<Double> (losingTradeCount != 0 ? grossLoss / losingTradeCount : 0));
        double rawl = grossLoss / losingTradeCount;
        result.setResult (MetricType.RATIO_AVE_WINNING_LOSING, new MetricValue (rawl != 0 ? Math.abs ((grossProfit / winningTradeCount) / (rawl)) : 0));

        result.setResult (MetricType.LARGEST_WINNING_TRADE, new MetricValue<Double> (maxWinning));
        result.setResult (MetricType.LARGEST_LOSING_TRADE, new MetricValue<Double> (maxLosing));

        result.setResult (MetricType.MAX_CONSECUTIVE_WINNING_TRADES, new MetricValue<Integer> (maxConsWinning));
        result.setResult (MetricType.MAX_CONSECUTIVE_LOSING_TRADES, new MetricValue<Integer> (maxConsLosing));
        result.setResult (MetricType.AVE_BARS_IN_WINNING_TRADES, new MetricValue<Double> (winningTradeCount != 0 ? winningBarCount / winningTradeCount : 0));
        result.setResult (MetricType.AVE_BARS_IN_LOSING_TRADES, new MetricValue<Double> (losingTradeCount != 0 ? losingBarCount / losingTradeCount : 0));

        result.setResult (MetricType.SHARPE, totalTradeCount != 0 ? (netProfitPercentage / totalTradeCount) / std : 0);
        result.setResult (MetricType.SORTINO, totalTradeCount != 0 ? (netProfitPercentage / totalTradeCount) / negativeStd : 0 );
    }
}
