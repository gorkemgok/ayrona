package com.ayronasystems.genetics.core;

/**
 * Created by gorkemgok on 29/03/16.
 */
public abstract class AbstractChromosome<T> implements Chromosome<T>{

    private double fitnessValue;

    private T gen;

    public AbstractChromosome (T gen) {
        this.gen = gen;
    }

    public double getFitnessValue () {
        return fitnessValue;
    }

    public void setFitnessValue (double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public T getGen () {
        return gen;
    }

    public void mutate (MutationMethod<T> mutationMethod, double probability) {
        gen = mutationMethod.apply (this, probability);
    }
}
