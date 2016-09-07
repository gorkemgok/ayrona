package com.ayronasystems.genetics.core.ast;

import java.util.List;

/**
 * Created by gorkemg on 28.03.2016.
 */
public interface NodePool {

    int getDepth();

    List<? extends Node> getNestedNodes ();

    List<? extends Node> getNestedNodes (NodeType type);

    List<? extends Node> getNestedNodes (NodeType type, int desiredDepth);

    CrossoverPoint getCrossoverPoint();

    CrossoverPoint getCrossoverPoint (NodeType type);

    CrossoverPoint getCrossoverPoint (NodeType type, int desiredDepth);

}

