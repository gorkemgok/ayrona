package com.ayronasystems.genetics.core.ast;

/**
 * Created by gorkemg on 28.03.2016.
 */
public interface Node extends Cloneable{

    NodeType getType();

    void setParentNode(FunctionNode parentNode);

    Node copy();

    int getDepth();
}
