package com.ayronasystems.genetics.core;

import java.util.*;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class TournamentSelection implements SelectionMethod{

    private final Comparator<Chromosome> comparator;

    private final int tournamentChromosomeCount;

    public TournamentSelection (int tournamentChromosomeCount) {
        this.tournamentChromosomeCount = tournamentChromosomeCount;
        this.comparator = Comparators.BIGGER_IS_FITTER;
    }

    public TournamentSelection (int tournamentChromosomeCount, Comparator<Chromosome> comparator) {
        this.tournamentChromosomeCount = tournamentChromosomeCount;
        this.comparator = comparator;
    }

    public List<ChromosomePair<Chromosome>> select (Population population) {
        int populationSize = population.getPopulationSize ();
        List<ChromosomePair<Chromosome>> selectedChromosomes = new ArrayList<ChromosomePair<Chromosome>> ();
        List<Chromosome> chromosomesToFight = new ArrayList<Chromosome> ();
        for ( int i = 0; i < populationSize; i++ ) {
            chromosomesToFight.clear ();
            for ( int j = 0; j < tournamentChromosomeCount; j++ ) {
                chromosomesToFight.add (population.getChromosome (Util.randomInt (populationSize - 1)));
            }
            selectedChromosomes.add (fight (chromosomesToFight));
        }
        return selectedChromosomes;
    }

    public ChromosomePair<Chromosome> selectOne (Population population) {
        int populationSize = population.getPopulationSize ();
        List<Chromosome> chromosomesToFight = new ArrayList<Chromosome> ();
        chromosomesToFight.clear ();
        for ( int j = 0; j < tournamentChromosomeCount; j++ ) {
            chromosomesToFight.add (population.getChromosome (Util.randomInt (populationSize - 1)));
        }
        return fight (chromosomesToFight);
    }

    private ChromosomePair<Chromosome> fight(List<Chromosome> chromosomeList){
        Collections.sort (chromosomeList, comparator);
        return new ChromosomePair<Chromosome> (chromosomeList.get (chromosomeList.size() - 1), chromosomeList.get (chromosomeList.size() - 2));
    }

    public Comparator<? extends Chromosome> getComparator() {
        return comparator;
    }
}
