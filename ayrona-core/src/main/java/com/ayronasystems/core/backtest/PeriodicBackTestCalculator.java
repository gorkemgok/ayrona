package com.ayronasystems.core.backtest;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.strategy.Position;

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
            return btr;
        }
        return null;
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
}
