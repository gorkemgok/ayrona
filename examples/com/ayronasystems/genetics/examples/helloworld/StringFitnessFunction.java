package com.ayronasystems.genetics.examples.helloworld;

import com.ayronasystems.genetics.core.FitnessFunction;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class StringFitnessFunction implements FitnessFunction<StringChromosome>{

    private String targetString;

    public StringFitnessFunction (String targetString) {
        this.targetString = targetString;
    }

    public double calculateFitness (StringChromosome chromosome) {
        double fitness = 0;
        String actualString = chromosome.getGen ();
        if (targetString.length () == actualString.length ()){
            fitness++;
        }
        int minLen = Math.min (targetString.length (), actualString.length ());
        for ( int i = 0; i < minLen; i++ ) {
            if ( chromosome.getGen ().charAt (i) == targetString.charAt (i)){
                fitness++;
            }
        }
        chromosome.setFitnessValue (fitness);
        return fitness;
    }
}
