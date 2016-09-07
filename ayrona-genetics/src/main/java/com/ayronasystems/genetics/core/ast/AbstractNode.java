package com.ayronasystems.genetics.core.ast;

/**
 * Created by gorkemgok on 02/04/16.
 */
public abstract class AbstractNode implements Node{

    private FunctionNode parentNode;

    public void setParentNode (FunctionNode parentNode) {
        this.parentNode = parentNode;
    }
}
