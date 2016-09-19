package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.genetics.core.ChromosomePair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class OptimizerCrossoverTest {

    @Test
    public void apply () throws Exception {
        Double[] parentGen1 = new Double[]{1d,2d,3d,4d,5d,6d};
        OptimizerChromosome optimizerChromosome1 = new OptimizerChromosome (parentGen1);
        Double[] parentGen2 = new Double[]{0d,0d,0d,0d,0d,0d};
        OptimizerChromosome optimizerChromosome2 = new OptimizerChromosome (parentGen2);

        OptimizerCrossover crossover = new OptimizerCrossover ();
        List<OptimizerChromosome> offsetList = crossover.apply (
                new ChromosomePair<OptimizerChromosome> (optimizerChromosome1, optimizerChromosome2)
        );

        Double[] gen1 = offsetList.get (0).getGen ();
        Double[] gen2 = offsetList.get (1).getGen ();
        int i;
        for ( i = 0; i < gen1.length; i++ ) {
            if (gen1[i] != parentGen1[i]){
                break;
            }
        }
        for ( int j = i; j < gen2.length; j++ ) {
            assertEquals (gen2[j], parentGen1[j]);
        }

        for ( i = 0; i < gen2.length; i++ ) {
            if (gen2[i] != parentGen2[i]){
                break;
            }
        }
        for ( int j = i; j < gen1.length; j++ ) {
            assertEquals (gen1[j], parentGen2[j]);
        }
    }

}