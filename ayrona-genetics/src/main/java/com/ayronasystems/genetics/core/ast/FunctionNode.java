package com.ayronasystems.genetics.core.ast;

/**
 * Created by gorkemgok on 30/03/16.
 */
public interface FunctionNode extends Node, NodePool {

    Node getChildNode (int index);

    boolean replaceChildNode(Node oldNode, Node newNode);

    void notifyObserversToAdd(Node node);

    void notifyObserversToRemove(Node node);

    void notifyObserversToReplace(Node oldNode, Node newNode);

}
