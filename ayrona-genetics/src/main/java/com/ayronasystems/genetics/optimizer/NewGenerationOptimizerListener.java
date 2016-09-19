package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.genetics.core.Population;
import com.ayronasystems.genetics.core.listener.ListenerContext;
import com.ayronasystems.genetics.core.listener.NewGenerationListener;

/**
 * Created by gorkemgok on 08/09/16.
 */
public class NewGenerationOptimizerListener implements NewGenerationListener<OptimizerChromosome>{

    public void onNewGeneration (ListenerContext<OptimizerChromosome> listenerContext) {
        Population<OptimizerChromosome> population = listenerContext.getPopulation ();
        System.out.println (population.getGenerationCount ()+".Gen("+listenerContext.getProcessTime ()+"ms):"+population.getFittest ());
    }
}
