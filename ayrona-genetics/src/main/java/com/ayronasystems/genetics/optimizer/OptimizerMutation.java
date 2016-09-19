package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.core.algo.OptimizerDef;
import com.ayronasystems.genetics.core.Chromosome;
import com.ayronasystems.genetics.core.Util;
import com.ayronasystems.genetics.core.MutationMethod;

import java.util.List;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class OptimizerMutation implements MutationMethod<Double[]>{

    private List<OptimizerDef> optimizerDefList;

    public OptimizerMutation (List<OptimizerDef> optimizerDefList) {
        this.optimizerDefList = optimizerDefList;
    }

    public Double[] apply (Chromosome<Double[]> chromosome, double probability) {
        Double[] gen = chromosome.getGen ();
        if (probability > Util.randomDouble ()){
            int mutationPoint = Util.randomInt (gen.length);
            double random = Util.getRandomDouble (optimizerDefList.get (mutationPoint));
            Double[] newGen = gen.clone ();
            newGen[mutationPoint] = random;
            return newGen;
        }
        return gen;
    }
}
