package com.ayronasystems.genetics.examples.helloworld;

import com.ayronasystems.genetics.core.AbstractChromosome;
import com.ayronasystems.genetics.core.Chromosome;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class StringChromosome extends AbstractChromosome<String> implements Chromosome<String> {

    private Chromosome<String> parent1;

    private Chromosome<String> parent2;

    public StringChromosome (String individual) {
        super (individual);
    }

    public void setParents (Chromosome<String> parent1, Chromosome<String> parent2) {
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    public Chromosome<String> getParent1 () {
        return parent1;
    }

    public Chromosome<String> getParent2 () {
        return parent2;
    }

    @Override
    public String toString () {
        return getGen ();
    }
}
