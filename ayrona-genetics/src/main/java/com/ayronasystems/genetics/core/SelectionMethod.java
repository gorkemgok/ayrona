package com.ayronasystems.genetics.core;

import java.util.Comparator;
import java.util.List;

/**
 * Created by gorkemg on 28.03.2016.
 */
public interface SelectionMethod {

    <C extends Chromosome> List<ChromosomePair<C>> select(Population<C> population);

    <C extends Chromosome> ChromosomePair<C> selectOne(Population<C> population);

    Comparator<? extends Chromosome> getComparator();
}
