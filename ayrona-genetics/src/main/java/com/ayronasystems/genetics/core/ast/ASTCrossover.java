package com.ayronasystems.genetics.core.ast;

import com.ayronasystems.genetics.core.ChromosomePair;
import com.ayronasystems.genetics.core.CrossoverMethod;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class ASTCrossover implements CrossoverMethod<ASTChromosome>{

    public List<ASTChromosome> apply (ChromosomePair<ASTChromosome> pair) {
        RootNode individual1 = pair.getChromosome1 ().getGen ().copy ();
        RootNode individual2 = pair.getChromosome2 ().getGen ().copy ();

        CrossoverPoint crossoverPoint1 = individual1.getCrossoverPoint ();
        CrossoverPoint crossoverPoint2 = individual2.getCrossoverPoint (crossoverPoint1.getNode ().getType (), crossoverPoint1.getNode ().getDepth ());

        if (crossoverPoint2 != null) {
            individual1.replaceChildNode (crossoverPoint1.getNode (), crossoverPoint2.getNode ());
            individual2.replaceChildNode (crossoverPoint2.getNode (), crossoverPoint1.getNode ());
        }

        return Arrays.asList (new ASTChromosome (individual1), new ASTChromosome (individual2));
    }
}
