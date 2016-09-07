package com.ayronasystems.genetics.core;

/**
 * Created by gorkemg on 28.03.2016.
 */
public class ChromosomePair<C extends Chromosome> {

    private C chromosome1;

    private C chromosome2;

    public ChromosomePair(C chromosome1, C chromosome2) {
        this.chromosome1 = chromosome1;
        this.chromosome2 = chromosome2;
    }

    public C getChromosome1 () {
        return chromosome1;
    }

    public C getChromosome2 () {
        return chromosome2;
    }
}
