package com.ayronasystems.genetics.core;

import com.ayronasystems.genetics.core.listener.NewFittestListener;
import com.ayronasystems.genetics.core.listener.NewGenerationListener;

/**
 * Created by gorkemg on 28.03.2016.
 */
public class GPConfiguration {

    private double mutationProbability;

    private int eliteCount = 3;

    private NewFittestListener newFittestListener;

    private NewGenerationListener newGenerationListener;

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public NewFittestListener getNewFittestListener () {
        return newFittestListener;
    }

    public void setNewFittestListener (NewFittestListener newFittestListener) {
        this.newFittestListener = newFittestListener;
    }

    public NewGenerationListener getNewGenerationListener () {
        return newGenerationListener;
    }

    public void setNewGenerationListener (NewGenerationListener newGenerationListener) {
        this.newGenerationListener = newGenerationListener;
    }

    public int getEliteCount() {
        return eliteCount;
    }

    public void setEliteCount(int eliteCount) {
        this.eliteCount = eliteCount;
    }
}
