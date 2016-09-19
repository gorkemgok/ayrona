package com.ayronasystems.genetics.core.listener;

import com.ayronasystems.genetics.core.Chromosome;
import com.ayronasystems.genetics.core.Population;

/**
 * Created by gorkemgok on 08/09/16.
 */
public class ListenerContext<C extends Chromosome> {

    private Population<C> population;

    private long processTime;

    public ListenerContext (Population<C> population, long processTime) {
        this.population = population;
        this.processTime = processTime;
    }

    public Population<C> getPopulation () {
        return population;
    }

    public long getProcessTime () {
        return processTime;
    }
}
