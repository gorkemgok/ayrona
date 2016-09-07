package com.ayronasystems.genetics.examples.bridge;

import com.ayronasystems.genetics.core.ast.NodeType;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class BPNodeTypes {

    private BPNodeTypes(){}

    public static final NodeType FN_CROSS = new NodeType (10);

    public static final NodeType FN_STOP = new NodeType (13);

    public static final NodeType FN_PERSON_SELECTOR = new NodeType (11);

    public static final NodeType TRM_PERSON_WITH = new NodeType (12);
}
