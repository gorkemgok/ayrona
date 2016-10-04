package com.ayronasystems.core.backtest;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.strategy.Position;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.*;

/**
 * Created by gorkemgok on 28/08/16.
 */
public class PeriodicBackTestCalculator implements BackTestCalculator {

    private PeriodicAggregator dayAgg = new PeriodicAggregator (ResultPeriod.DAY);
    private PeriodicAggregator weekAgg = new PeriodicAggregator (ResultPeriod.WEEK);
    private PeriodicAggregator monthAgg = new PeriodicAggregator (ResultPeriod.MONTH);
    private PeriodicAggregator yearAgg = new PeriodicAggregator (ResultPeriod.YEAR);
    private PeriodicAggregator infinityAgg = new PeriodicAggregator (ResultPeriod.INFINITY);

    private List<PeriodicAggregator> periodicAggregatorList = new ArrayList<PeriodicAggregator> (
            Arrays.asList (new PeriodicAggregator[]{
                    dayAgg,
                    weekAgg,
                    monthAgg,
                    yearAgg,
                    infinityAgg
            })
    );

    public BackTestResult calculate (List<Position> positionList, MarketData marketData) {
        if (positionList.size () > 0 && marketData.size () > 0) {
            List<Position> currentPositions = new ArrayList<Position> ();
            int posIdx = 0;
            Position position = positionList.get (posIdx);
            for ( int i = 0; i < marketData.size (); i++ ) {
                Date date = marketData.getDate (i);
                forwardDate (date);
                double low = marketData.getPrice (PriceColumn.LOW, i);
                //Set current positions
                while ( date.equals (position.getOpenDate ()) && posIdx < positionList.size ()){
                    currentPositions.add (position);
                    posIdx++;
                    if (posIdx < positionList.size ()) {
                        position = positionList.get (posIdx);
                    }
                }
                //Remove closed positions
                Iterator<Position> iterator = currentPositions.iterator ();
                while (iterator.hasNext ()){
                    Position currentPosition = iterator.next ();
                    if (currentPosition.isClosed () && date.after (currentPosition.getCloseDate ())){
                        double profit = currentPosition.calculateProfit ();
                        double profitPercentage = currentPosition.calculateProfitPercentage ();
                        addProfit (profit, profitPercentage);
                        iterator.remove ();
                    }
                }

                for (Position currentPosition : currentPositions){
                    double mdd;
                    double mddPercentage;
                    if (currentPosition.getDirection () == Direction.LONG){
                        mdd = low - currentPosition.getOpenPrice ();
                    }else{
                        mdd = currentPosition.getOpenPrice () - low;
                    }
                    mddPercentage = (mdd / currentPosition.getOpenPrice ()) * 100;
                    addMdd (mdd, mddPercentage);
                }
            }
            BackTestResult btr = new BackTestResult (marketData.getBeginningDate (), marketData.getEndingDate ());
            btr.setPositionList (positionList);
            Map<ResultPeriod, List<ResultQuanta>> resultMap = new HashMap<ResultPeriod, List<ResultQuanta>> ();
            for(PeriodicAggregator periodicAggregator : periodicAggregatorList){
                periodicAggregator.endPeriod ();
                resultMap.put (periodicAggregator.getResultPeriod (), periodicAggregator.getResultList ());
            }
            btr.setPeriodicResultMap (resultMap);
            setBTRMetricsByInfinity (btr);
            return btr;
        }
        return null;
    }

    private double calculateSTD(List<ResultQuanta> resultQuantaList){
        try {
            int size = resultQuantaList.size ();
            StandardDeviation std = new StandardDeviation ();
            double[] values = new double[size];
            for ( int i = 0; i < size; i++ ) {
                values[i] = resultQuantaList.get (i)
                                            .getValue (ResultQuantaMetric.NET_PROFIT_PERCENTAGE);
            }
            return std.evaluate (values);
        }catch ( NoDataException ex){
            return Double.NaN;
        }
    }

    private double calculateRSquared(List<ResultQuanta> resultQuantaList){
        try {
            int size = resultQuantaList.size ();
            SimpleRegression regression = new SimpleRegression ();
            for ( int i = 0; i < size; i++ ) {
                regression.addData (i, resultQuantaList.get (i)
                                                       .getValue (ResultQuantaMetric.EQUITY));
            }
            return regression.regress ()
                             .getRSquared ();
        }catch ( NoDataException ex){
            return Double.NaN;
        }
    }

    private void forwardDate(Date date){
        for(PeriodicAggregator periodicAggregator : periodicAggregatorList) {
            periodicAggregator.forwardDate (date);
        }
    }

    private void addProfit(double profit, double profitPercentage){
        for(PeriodicAggregator periodicAggregator : periodicAggregatorList){
            periodicAggregator.addProfit (profit, profitPercentage);
        }
    }

    private void addMdd(double mdd, double mddPercentage){
        for(PeriodicAggregator periodicAggregator : periodicAggregatorList){
            periodicAggregator.addMdd (mdd, mddPercentage);
        }
    }

    private void setBTRMetricsByInfinity(BackTestResult btr){
        List<ResultQuanta> resultQuantaList = btr.getPeriodicResultMap ().get (ResultPeriod.INFINITY);
        if (resultQuantaList != null && resultQuantaList.size () > 0) {
            ResultQuanta resultQuanta = resultQuantaList.get (0);
            btr.setResult (MetricType.NET_PROFIT, resultQuanta.getValue (ResultQuantaMetric.NET_PROFIT));
            btr.setResult (MetricType.NET_PROFIT_PERCENTAGE, resultQuanta.getValue (ResultQuantaMetric.NET_PROFIT_PERCENTAGE));
            btr.setResult (MetricType.GROSS_PROFIT, resultQuanta.getValue (ResultQuantaMetric.GROSS_PROFIT));
            btr.setResult (MetricType.GROSS_PROFIT_PERCENTAGE, resultQuanta.getValue (ResultQuantaMetric.GROSS_PROFIT_PERCENTAGE));
            btr.setResult (MetricType.GROSS_LOSS, resultQuanta.getValue (ResultQuantaMetric.GROSS_LOSS));
            btr.setResult (MetricType.GROSS_LOSS_PERCENTAGE, resultQuanta.getValue (ResultQuantaMetric.GROSS_LOSS_PERCENTAGE));
            btr.setResult (MetricType.PROFIT_FACTOR, resultQuanta.getValue (ResultQuantaMetric.PROFIT_FACTOR));

            btr.setResult (MetricType.TOTAL_NUMBER_OF_TRADES, resultQuanta.getValue (ResultQuantaMetric.TOTAL_NUMBER_OF_TRADES));
            btr.setResult (MetricType.PROFITABLE_PERCENT, resultQuanta.getValue (ResultQuantaMetric.PROFITABLE_PERCENT));
            btr.setResult (MetricType.WINNING_TRADE_COUNT, resultQuanta.getValue (ResultQuantaMetric.WINNING_TRADE_COUNT));
            btr.setResult (MetricType.LOSING_TRADE_COUNT, resultQuanta.getValue (ResultQuantaMetric.LOSING_TRADE_COUNT));

            btr.setResult (MetricType.AVE_TRADE_NET_PROFIT, resultQuanta.getValue (ResultQuantaMetric.AVE_NET_PROFIT));
            btr.setResult (MetricType.AVE_TRADE_NET_PROFIT_PERCENTAGE, resultQuanta.getValue (ResultQuantaMetric.AVE_NET_PROFIT_PERCENT));
            btr.setResult (MetricType.AVE_WINNING_TRADE, resultQuanta.getValue (ResultQuantaMetric.AVE_WINNING_TRADE));
            btr.setResult (MetricType.AVE_WINNING_TRADE_PERCENTAGE, resultQuanta.getValue (ResultQuantaMetric.AVE_WINNING_TRADE_PERCENT));
            btr.setResult (MetricType.AVE_LOSING_TRADE, resultQuanta.getValue (ResultQuantaMetric.AVE_LOSING_TRADE));
            btr.setResult (MetricType.AVE_LOSING_TRADE_PERCENTAGE, resultQuanta.getValue (ResultQuantaMetric.AVE_LOSING_TRADE_PERCENT));

            btr.setResult (MetricType.MAX_TRADE_DRAWDOWN, resultQuanta.getValue (ResultQuantaMetric.MDD));
            btr.setResult (MetricType.MAX_TRADE_DRAWDOWN_PERCENTAGE, resultQuanta.getValue (ResultQuantaMetric.MDD_PERCENTAGE));

            btr.setResult (MetricType.DAILY_STD, calculateSTD (btr.getPeriodicResultMap ().get (ResultPeriod.DAY)));
            btr.setResult (MetricType.DAILY_RSQUARED, calculateRSquared (btr.getPeriodicResultMap ().get (ResultPeriod.DAY)));
            btr.setResult (MetricType.WEEKLY_STD, calculateSTD (btr.getPeriodicResultMap ().get (ResultPeriod.WEEK)));
            btr.setResult (MetricType.WEEKLY_RSQUARED, calculateRSquared (btr.getPeriodicResultMap ().get (ResultPeriod.WEEK)));
            btr.setResult (MetricType.MONTHLY_STD, calculateSTD (btr.getPeriodicResultMap ().get (ResultPeriod.MONTH)));
            btr.setResult (MetricType.MONTHLY_RSQUARED, calculateRSquared (btr.getPeriodicResultMap ().get (ResultPeriod.MONTH)));
            btr.setResult (MetricType.YEARLY_STD, calculateSTD (btr.getPeriodicResultMap ().get (ResultPeriod.YEAR)));
            btr.setResult (MetricType.YEARLY_RSQUARED, calculateRSquared (btr.getPeriodicResultMap ().get (ResultPeriod.YEAR)));

        }
    }
}
