package com.ayronasystems.genetics.core.ast;

import com.ayronasystems.genetics.core.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 02/04/16.
 */
public abstract class AbstractFunctionNode implements FunctionNode{

    private FunctionNode parentNode;

    public List<? extends Node> getNestedNodes (NodeType type) {
        List<Node> nodeListByType =  new ArrayList<Node> ();
        for ( Node node :
                getNestedNodes () ) {
            if (node.getType ().equals (type)){
                nodeListByType.add (node);
            }

        }
        return nodeListByType;
    }

    public List<? extends Node> getNestedNodes (NodeType type, int desiredDepth) {
        List<Node> nodeListByType =  new ArrayList<Node> ();
        for ( Node node :
                getNestedNodes () ) {
            if (node.getType ().equals (type) && node.getDepth () <= desiredDepth){
                nodeListByType.add (node);
            }

        }
        return nodeListByType;
    }

    public Node getChildNode (int index) {
        return getNestedNodes ().get (index);
    }

    public CrossoverPoint getCrossoverPoint () {
        int randomIndex = Util.randomInt (getNestedNodes ().size () - 1 );
        return new CrossoverPoint (randomIndex, getNestedNodes ().get (randomIndex));
    }

    public CrossoverPoint getCrossoverPoint (NodeType type) {
        List<? extends Node> allNodesByType = getNestedNodes (type);
        if (allNodesByType.size () > 1){
            int randomIndex = Util.randomInt ( allNodesByType.size () - 1 );
            Node node = allNodesByType.get (randomIndex);
            return new CrossoverPoint (getNestedNodes ().indexOf (node), node);
        }else if (allNodesByType.size () == 1){
            return new CrossoverPoint (getNestedNodes ().indexOf (0), allNodesByType.get (0));
        }
        return null;
    }

    public CrossoverPoint getCrossoverPoint (NodeType type, int depth) {
        List<? extends Node> allNodesByType = getNestedNodes (type, depth);
        if (allNodesByType.size () > 1){
            int randomIndex = Util.randomInt ( allNodesByType.size () - 1 );
            Node node = allNodesByType.get (randomIndex);
            return new CrossoverPoint (getNestedNodes ().indexOf (node), node);
        }else if (allNodesByType.size () == 1){
            return new CrossoverPoint (getNestedNodes ().indexOf (0), allNodesByType.get (0));
        }
        return null;
    }

    public void notifyObserversToAdd (Node node) {
        if ( parentNode != null ) parentNode.notifyObserversToAdd (node);
    }

    public void notifyObserversToRemove (Node node) {
        if ( parentNode != null ) parentNode.notifyObserversToRemove (node);
    }

    public void notifyObserversToReplace (Node oldNode, Node newNode) {
        if ( parentNode != null ) parentNode.notifyObserversToReplace (newNode, oldNode);
    }

    public void setParentNode (FunctionNode parentNode) {
        this.parentNode = parentNode;
    }
}
