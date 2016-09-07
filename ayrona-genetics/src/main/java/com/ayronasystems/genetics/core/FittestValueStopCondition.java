package com.ayronasystems.genetics.core;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class FittestValueStopCondition implements StopCondition{

    private double targetFittestValue;

    public FittestValueStopCondition (double targetFittestValue) {
        this.targetFittestValue = targetFittestValue;
    }

    public boolean satisfy (Population population) {
        Chromosome fittest = population.getFittest ();
        if ( fittest != null && targetFittestValue <= fittest.getFitnessValue () ){
            return true;
        }
        return false;
    }
}
