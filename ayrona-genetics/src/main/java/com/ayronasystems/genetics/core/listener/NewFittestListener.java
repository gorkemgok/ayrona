package com.ayronasystems.genetics.core.listener;

import com.ayronasystems.genetics.core.Chromosome;
import com.ayronasystems.genetics.core.Population;

/**
 * Created by gorkemgok on 29/03/16.
 */
public interface NewFittestListener<C extends Chromosome> {

    void onNewFittest(Population<C> population);

}
