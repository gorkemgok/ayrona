package com.ayronasystems.genetics.core.ast;

import com.ayronasystems.genetics.core.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class RootNode extends AbstractFunctionNode {

    public static final NodeType ROOT_NODE = new NodeType (-1);

    private List<Node> nodes = new ArrayList<Node>();

    public RootNode (Node... nodes) {
        for ( Node node :
                nodes ) {
            this.nodes.add (node);
            node.setParentNode (this);
        }
    }

    public boolean replaceChildNode (Node oldNode, Node newNode) {
        newNode.setParentNode (this);
        int index = nodes.indexOf (oldNode);
        if (index > -1){
            nodes.set (index, newNode);
            return true;
        }else{
            for ( Node node :
                    nodes ) {
                if (node instanceof FunctionNode){
                    if (((FunctionNode)node).replaceChildNode (oldNode, newNode)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public NodeType getType () {
        return ROOT_NODE;
    }

    public List<Node> getNestedNodes () {
        return Util.getAllNestedNodes (nodes);
    }

    @Override
    public String toString () {
        return "RootNode{" +
                "nodes=" + nodes +
                '}';
    }

    public RootNode copy () {
        RootNode rootNode = new RootNode ();
        for ( Node node :
                nodes ) {
            rootNode.nodes.add (node.copy ());
        }
        return rootNode;
    }

    public int getDepth () {
        int depth = 0;
        for ( Node node :
                nodes ) {
            depth = Math.max (node.getDepth (), depth);
        }
        return depth + 1;
    }
}
