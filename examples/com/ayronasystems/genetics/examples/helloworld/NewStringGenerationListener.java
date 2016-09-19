package com.ayronasystems.genetics.examples.helloworld;

import com.ayronasystems.genetics.core.Population;
import com.ayronasystems.genetics.core.listener.ListenerContext;
import com.ayronasystems.genetics.core.listener.NewGenerationListener;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class NewStringGenerationListener implements NewGenerationListener<StringChromosome>{

    public void onNewGeneration (ListenerContext<StringChromosome> listenerContext) {
        Population population = listenerContext.getPopulation ();
        System.out.println(population.getGenerationCount ()
                                   +". Generation with "+population.getPopulationSize ()+" ind. fittest: "
                                   +population.getFittest ().getGen ()
                                   +" ("+population.getFittest ().getFitnessValue ()+" )"
        );
    }
}
