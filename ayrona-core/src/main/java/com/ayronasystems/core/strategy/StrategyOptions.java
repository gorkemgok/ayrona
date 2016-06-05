package com.ayronasystems.core.strategy;

import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.definition.commission.Commission;
import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;
import org.joda.time.Interval;

/**
 * Created by gorkemgok on 15/03/15.
 */
public class StrategyOptions {

    private SymbolTimeSeries baseSeries;

    private Commission commission;

    private Interval trainingInterval;

    private Interval testInterval;

    private Interval validationInterval;

    private boolean twoWayPositionsAllowed = false;

    private boolean processHoldAsOpposite = true;

    private boolean processBothAsOpposite = true;

    private MetricType fitnessMetric = MetricType.NET_PROFIT;

    public static class Builder{

        private StrategyOptions strategyOptions;

        public Builder () {
            strategyOptions = new StrategyOptions ();
        }

        public Builder baseSeries(SymbolTimeSeries baseSeries){
            strategyOptions.baseSeries = baseSeries;
            return this;
        }

        public Builder commission(Commission commission){
            strategyOptions.commission = commission;
            return this;
        }

        public Builder trainingInterval(Interval interval){
            strategyOptions.trainingInterval = interval;
            return this;
        }

        public Builder testInterval(Interval interval){
            strategyOptions.testInterval = interval;
            return this;
        }

        public Builder validationInterval(Interval interval){
            strategyOptions.validationInterval = interval;
            return this;
        }

        public Builder isTwoWayPositionAllowed(boolean allowed){
            strategyOptions.twoWayPositionsAllowed = allowed;
            return this;
        }

        public Builder processHoldAsOpposite (boolean allowed){
            strategyOptions.processHoldAsOpposite = allowed;
            return this;
        }

        public Builder processBothAsOpposite (boolean allowed){
            strategyOptions.processBothAsOpposite = allowed;
            return this;
        }

        public Builder fitnessMetric(MetricType fitnessMetric){
            strategyOptions.fitnessMetric = fitnessMetric;
            return this;
        }

        public StrategyOptions build(){
            return strategyOptions;
        }
    }

    private StrategyOptions (){

    }

    public boolean isProcessHoldAsOpposite() {
        return processHoldAsOpposite;
    }

    public boolean isProcessBothAsOpposite() {
        return processBothAsOpposite;
    }

    public SymbolTimeSeries getBaseSeries() {
        return baseSeries;
    }

    public Commission getCommission() {
        return commission;
    }

    public Interval getTrainingInterval () {
        return trainingInterval;
    }

    public Interval getTestInterval () {
        return testInterval;
    }

    public Interval getValidationInterval () {
        return validationInterval;
    }

    public boolean isTwoWayPositionsAllowed() {
        return twoWayPositionsAllowed;
    }

    public MetricType getFitnessMetric () {
        return fitnessMetric;
    }

    public void setBaseSeries (SymbolTimeSeries baseSeries) {
        this.baseSeries = baseSeries;
    }

    public void setCommission (Commission commission) {
        this.commission = commission;
    }

    public void setTrainingInterval (Interval trainingInterval) {
        this.trainingInterval = trainingInterval;
    }

    public void setTestInterval (Interval testInterval) {
        this.testInterval = testInterval;
    }

    public void setValidationInterval (Interval validationInterval) {
        this.validationInterval = validationInterval;
    }

    public void setTwoWayPositionsAllowed (boolean twoWayPositionsAllowed) {
        this.twoWayPositionsAllowed = twoWayPositionsAllowed;
    }

    public void setProcessHoldAsOpposite (boolean processHoldAsOpposite) {
        this.processHoldAsOpposite = processHoldAsOpposite;
    }

    public void setProcessBothAsOpposite (boolean processBothAsOpposite) {
        this.processBothAsOpposite = processBothAsOpposite;
    }

    public void setFitnessMetric (MetricType fitnessMetric) {
        this.fitnessMetric = fitnessMetric;
    }

}
