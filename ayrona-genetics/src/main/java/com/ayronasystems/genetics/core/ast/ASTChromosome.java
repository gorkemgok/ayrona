package com.ayronasystems.genetics.core.ast;

import com.ayronasystems.genetics.core.AbstractChromosome;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class ASTChromosome extends AbstractChromosome<RootNode> {

    public ASTChromosome (RootNode individual) {
        super (individual);
    }

    @Override
    public String toString () {
        return getGen ().toString ();
    }
}
