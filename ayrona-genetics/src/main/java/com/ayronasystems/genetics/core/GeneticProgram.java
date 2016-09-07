package com.ayronasystems.genetics.core;

import com.ayronasystems.genetics.core.listener.NewFittestListener;
import com.ayronasystems.genetics.core.listener.NewGenerationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 28.03.2016.
 */
public class GeneticProgram {

    private final Population<Chromosome> population;

    private final SelectionMethod selectionMethod;

    private final CrossoverMethod<Chromosome> crossoverMethod;

    private final MutationMethod mutationMethod;

    private final FitnessFunction fitnessFunction;

    private final StopCondition stopCondition;

    private final GPConfiguration configuration;

    public GeneticProgram(Population population, SelectionMethod selectionMethod, CrossoverMethod crossoverMethod, MutationMethod mutationMethod, FitnessFunction<Chromosome> fitnessFunction, StopCondition stopCondition, GPConfiguration configuration) {
        this.population = population;
        this.selectionMethod = selectionMethod;
        this.crossoverMethod = crossoverMethod;
        this.mutationMethod = mutationMethod;
        this.fitnessFunction = fitnessFunction;
        this.stopCondition = stopCondition;
        this.configuration = configuration;
    }

    public void evolve(){
        long start = System.currentTimeMillis ();
        NewFittestListener newFittestListener = configuration.getNewFittestListener ();
        NewGenerationListener newGenerationListener = configuration.getNewGenerationListener ();
        double mutationProbability = configuration.getMutationProbability ();
        while (!stopCondition.satisfy(population)){
            //Calculate fitness
            for (Chromosome chromosome : population){
                chromosome.setFitnessValue (fitnessFunction.calculateFitness(chromosome));
                //System.out.println(chromosome.getGen ()+ " ("+chromosome.getFitnessValue ()+ ")");
                if (population.compareWithFittest (chromosome)){
                    if (newFittestListener != null) {
                        newFittestListener.onNewFittest (population);
                    }
                }
            }
            population.sort();

            //Apply crossover to Individual pairs
            List<Chromosome> offsetChromosomesList = new ArrayList<Chromosome>(population.getPopulationSize());
            int eliteCount = configuration.getEliteCount();
            int ii = population.getPopulationSize() - 1;
            for (int i = ii; i > ii - eliteCount; i--) {
                offsetChromosomesList.add(population.getChromosome(i));
            }
            for (int i = 0; i < population.getPopulationSize () - eliteCount; i++){
                //Select Individual pairs for sex
                ChromosomePair<Chromosome> chromosomePair = selectionMethod.selectOne(population);
                List<Chromosome> newBornChromosomes = crossoverMethod.apply(chromosomePair);
                if (mutationMethod != null) {
                    newBornChromosomes.get (0)
                                      .mutate (mutationMethod, mutationProbability);
                }
                offsetChromosomesList.add(newBornChromosomes.get (0));
            }

            //Make way for the young
            population.replacePopulation(offsetChromosomesList);
            if (newGenerationListener != null){
                newGenerationListener.onNewGeneration (population);
            }
        }
        long end = System.currentTimeMillis ();
        System.out.println ("Stop condition reached at "+population.getGenerationCount ()+". generation in "+(end-start)+" ms");
    }
}
