package com.ayronasystems.genetics.core.listener;

import com.ayronasystems.genetics.core.Chromosome;

/**
 * Created by gorkemgok on 29/03/16.
 */
public interface NewGenerationListener<C extends Chromosome> {

    void onNewGeneration(ListenerContext<C> listenerContext);
}
