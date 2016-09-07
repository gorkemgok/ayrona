package com.ayronasystems.genetics.examples.ayronaoptimizer;

import com.ayronasystems.genetics.core.listener.NewFittestListener;
import com.ayronasystems.genetics.core.Population;

import java.text.DecimalFormat;

/**
 * Created by gorkemgok on 22/07/16.
 */
public class NewFittestOptimizerListener implements NewFittestListener<OptimizerChromosome> {

    public void onNewFittest (Population<OptimizerChromosome> population) {
        OptimizerChromosome fittest = population.getFittest ();
        System.out.println ("NEW FITTEST: "+new DecimalFormat ("##.##").format (fittest.getFitnessValue ())+" : "+fittest);
    }
}
