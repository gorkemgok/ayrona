package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.genetics.core.AbstractChromosome;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class OptimizerChromosome extends AbstractChromosome<Double[]> {

    public OptimizerChromosome (Double[] individual) {
        super (individual);
    }

    @Override
    public String toString () {
        Double[] gen = getGen ();
        StringBuilder sb = new StringBuilder ("OptimizerChromosome{");
        for( Double d : gen){
            sb.append (d).append (", ");
        }
        sb.append ("}");
        return sb.toString ();
    }
}
