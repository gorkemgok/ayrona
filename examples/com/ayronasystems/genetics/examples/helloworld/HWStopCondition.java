package com.ayronasystems.genetics.examples.helloworld;

import com.ayronasystems.genetics.core.FittestValueStopCondition;
import com.ayronasystems.genetics.core.Population;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class HWStopCondition extends FittestValueStopCondition {

    private int maxGenerationCount = 0;

    public HWStopCondition (double targetFittestValue, int maxGenerationCount) {
        super (targetFittestValue);
        this.maxGenerationCount = maxGenerationCount;
    }

    @Override
    public boolean satisfy (Population population) {
        return super.satisfy (population) || (maxGenerationCount != 0 && maxGenerationCount <= population.getGenerationCount ());
    }
}
