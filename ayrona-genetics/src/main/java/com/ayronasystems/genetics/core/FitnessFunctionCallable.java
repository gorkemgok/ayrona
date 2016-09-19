package com.ayronasystems.genetics.core;

import com.ayronasystems.genetics.core.listener.ListenerContext;
import com.ayronasystems.genetics.core.listener.NewFittestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by gorkemgok on 08/09/16.
 */
public class FitnessFunctionCallable implements Callable<Double>{

    private static Logger log = LoggerFactory.getLogger (FitnessFunctionCallable.class);

    private Population population;

    private FitnessFunction fitnessFunction;

    private Chromosome chromosome;

    private NewFittestListener newFittestListener;

    public FitnessFunctionCallable (Population population, FitnessFunction fitnessFunction, Chromosome chromosome, NewFittestListener newFittestListener) {
        this.population = population;
        this.fitnessFunction = fitnessFunction;
        this.chromosome = chromosome;
        this.newFittestListener = newFittestListener;
    }

    public Double call () throws Exception {
        try {
            long s1 = System.currentTimeMillis ();
            double fitness = fitnessFunction.calculateFitness (chromosome);
            chromosome.setFitnessValue (fitness);
            long e1 = System.currentTimeMillis ();
            //System.out.println(chromosome.getGen ()+ " ("+chromosome.getFitnessValue ()+ ")");
            if ( population.compareWithFittest (chromosome) ) {
                if ( newFittestListener != null ) {
                    newFittestListener.onNewFittest (new ListenerContext (population, (e1 - s1)));
                }
            }
            return fitness;
        }catch ( Exception ex ){
            log.error ("Fitness calculation exception", ex);
            throw  ex;
        }
    }
}
