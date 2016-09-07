package com.ayronasystems.genetics.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gorkemgok on 28.03.2016.
 */
public class Population<C extends Chromosome> implements Iterable<C>{

    private C fittestChromosome;

    private int generationCount;

    private List<C> individuals;

    private final Comparator<Chromosome> comparator;

    public Population(List<C> individuals) {
        this.individuals = individuals;
        this.comparator = Comparators.BIGGER_IS_FITTER;
    }

    public Population (List<C> individuals, Comparator<Chromosome> comparator) {
        this.individuals = individuals;
        this.comparator = comparator;
    }

    public void replacePopulation(List<C> newIndividuals){
        individuals = newIndividuals;
        generationCount++;
    }

    public int getGenerationCount() {
        return generationCount;
    }

    public Iterator<C> iterator() {
        return individuals.iterator ();
    }

    public int getPopulationSize() {
        return individuals.size();
    }

    public C getChromosome(int index){
        return individuals.get (index);
    }

    public C getFittest(){
        return fittestChromosome;
    }

    public boolean compareWithFittest(C chromosome){
        if (fittestChromosome == null || comparator.compare (fittestChromosome, chromosome) == -1){
            fittestChromosome = chromosome;
            return true;
        }
        return false;
    }

    public void sort(){
        Collections.sort(individuals, comparator);
    }
}
