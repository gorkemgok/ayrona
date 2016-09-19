package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.genetics.core.Util;
import com.ayronasystems.genetics.core.ChromosomePair;
import com.ayronasystems.genetics.core.CrossoverMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class OptimizerCrossover implements CrossoverMethod<OptimizerChromosome>{

    public List<OptimizerChromosome> apply (ChromosomePair<OptimizerChromosome> pair) {
        Double[] gen1 = pair.getChromosome1 ().getGen ();
        Double[] gen2 = pair.getChromosome2 ().getGen ();
        int length = Math.min(gen1.length, gen2.length);
        int crossoverPoint = Util.randomInt (length - 2) + 1;
        List<OptimizerChromosome> offsetList = new ArrayList<OptimizerChromosome> ();
        offsetList.add (
                new OptimizerChromosome (crossover (gen1, gen2, crossoverPoint))
        );
        offsetList.add (
                new OptimizerChromosome (crossover (gen2, gen1, crossoverPoint))
        );
        return offsetList;
    }

    private Double[] crossover(Double[] gen1, Double[] gen2, int crossoverPoint){
        Double[] offsetGen = new Double[gen2.length];
        int j = 0;
        for ( int i = 0; i < crossoverPoint; i++ ) {
            offsetGen[j] = gen1[i];
            j++;
        }
        for ( int i = crossoverPoint; i < gen2.length; i++ ) {
            offsetGen[j] = gen2[i];
            j++;
        }
        return offsetGen;
    }
}
