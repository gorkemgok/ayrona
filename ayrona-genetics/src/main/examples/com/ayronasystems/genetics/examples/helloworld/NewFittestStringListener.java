package com.ayronasystems.genetics.examples.helloworld;

import com.ayronasystems.genetics.core.listener.NewFittestListener;
import com.ayronasystems.genetics.core.Population;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class NewFittestStringListener implements NewFittestListener<StringChromosome> {

    public void onNewFittest (Population<StringChromosome> population) {
        StringChromosome newFittestChromosome = population.getFittest ();
        String p1 = newFittestChromosome.getParent1 () != null ? newFittestChromosome.getParent1 ().getGen () : "";
        String p2 = newFittestChromosome.getParent2 () != null ? newFittestChromosome.getParent2 ().getGen () : "";
        System.out.println("New Fittest at "+population.getGenerationCount ()+". gen., "+p1 + " X "+ p2 +" = "+ newFittestChromosome.toString () + "  ("+newFittestChromosome.getFitnessValue ()+" )");
    }

}
