package com.ayronasystems.genetics.core;

import java.util.List;

/**
 * Created by gorkemg on 28.03.2016.
 */
public interface CrossoverMethod<C extends Chromosome> {

    List<C> apply(ChromosomePair<C> pair);

}
