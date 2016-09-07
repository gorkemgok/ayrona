package com.ayronasystems.genetics.examples.bridge;

import com.ayronasystems.genetics.core.Util;
import com.ayronasystems.genetics.core.ast.AbstractFunctionNode;
import com.ayronasystems.genetics.core.ast.FunctionNode;
import com.ayronasystems.genetics.core.ast.Node;
import com.ayronasystems.genetics.core.ast.NodeType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class Move extends AbstractFunctionNode implements FunctionNode {

    public enum Type{
        STOP,
        CROSS;
    }

    private Type type;

    private PersonSelector personSelector;

    private Move nextCross;

    public Move (Type type) {
        this.type = type;
    }

    public Move getNextMove () {
        return nextCross;
    }

    public void setNextMove (Move nextCross) {
        this.nextCross = nextCross;
        nextCross.setParentNode (this);
    }

    public PersonSelector getPersonSelector () {
        return personSelector;
    }

    public void setPersonSelector (PersonSelector personSelector) {
        this.personSelector = personSelector;
        personSelector.setParentNode (this);
    }

    public NodeType getType () {
        if (type == Type.CROSS) {
            return BPNodeTypes.FN_CROSS;
        }else {
            return BPNodeTypes.FN_STOP;
        }
    }

    public Type getMoveType(){
        return type;
    }

    public List<Node> getNestedNodes () {
        return Util.getAllNestedNodes (Arrays.asList (nextCross, personSelector));
    }

    public boolean replaceChildNode (Node oldNode, Node newNode) {
        if (type.equals (Type.STOP)){
            return false;
        }
        if (oldNode.equals (nextCross)){
            nextCross = (Move)newNode;
            return true;
        }else if (oldNode.equals (personSelector)){
            personSelector = (PersonSelector)newNode;
            return true;
        }else if (!nextCross.replaceChildNode (oldNode, newNode)){
            return personSelector.replaceChildNode (oldNode, newNode);
        }else {
            return true;
        }
    }

    @Override
    public String toString () {
        return "Move{" +
                "type=" + type +
                ", personSelector=" + personSelector +
                ", nextCross=" + nextCross +
                '}';
    }

    public Move copy () {
        Move move = new Move(type);
        if (personSelector != null) move.setPersonSelector (personSelector.copy ());
        if (nextCross != null) move.setNextMove (nextCross.copy ());
        return move;
    }

    public int getDepth () {
        if (type == Type.STOP){
            return 0;
        }else{
            int depth = Math.max (nextCross.getDepth (), personSelector.getDepth ());
            return depth + 1;
        }
    }

}
