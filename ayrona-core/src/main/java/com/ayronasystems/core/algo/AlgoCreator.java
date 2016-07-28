package com.ayronasystems.core.algo;

import com.ayronasystems.core.algo.tree.FunctionNode;
import com.ayronasystems.core.algo.tree.MarketDataNode;
import com.ayronasystems.core.algo.tree.Node;
import com.ayronasystems.core.definition.PriceColumn;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by gorkemgok on 25/05/16.
 */
public class AlgoCreator {

    private List<OptimizerDef> optimizerDefList = new ArrayList<OptimizerDef> ();

    private List<Double> optimizedValueList;

    private int currentIndex;

    public String NAME;

    public Node BUY;

    public Node SELL;

    public final Node O = new MarketDataNode (PriceColumn.OPEN);
    public final Node H = new MarketDataNode (PriceColumn.HIGH);
    public final Node L = new MarketDataNode (PriceColumn.LOW);
    public final Node C = new MarketDataNode (PriceColumn.CLOSE);

    public int getNeededInputCount () {
        return Math.max (BUY.getNeededInputCount (), SELL.getNeededInputCount ());
    };

    public String getName () {
        return NAME;
    }

    public Node SMA(Node input, int period){
        return new FunctionNode ("SMA", input, new double[]{period});
    }

    public Node SAR(double acceleration, double maximum){
        return new FunctionNode ("SAR", Arrays.asList (new MarketDataNode (PriceColumn.HIGH), new MarketDataNode(PriceColumn.LOW)), new double[]{acceleration, maximum});
    }

    public Node SAR(double acceleration, double maximum, int size){
        return new FunctionNode ("SAR",
                                 Arrays.asList (
                                         new FunctionNode ("X",
                                                 new MarketDataNode (PriceColumn.HIGH),
                                                 size),
                                         new FunctionNode ("X",
                                                           new MarketDataNode (PriceColumn.LOW),
                                                           size)
                                 ),
                                 new double[]{acceleration, maximum});
    }

    public Node AND(Node node1, Node node2){
        return new FunctionNode ("AND", Arrays.asList (node1, node2));
    }

    public Node OR(Node node1, Node node2){
        return new FunctionNode ("OR", Arrays.asList (node1, node2));
    }

    public Node LT(Node node1, Node node2){
        return new FunctionNode ("LT", Arrays.asList (node1, node2));
    }

    public Node GT(Node node1, Node node2){
        return new FunctionNode ("GT", Arrays.asList (node1, node2));
    }

    public Node LT(Node node, double value){
        return new FunctionNode ("LT", node, new double[]{value});
    }

    public Node GT(Node node, double value){
        return new FunctionNode ("GT", node, new double[]{value});
    }

    public Node ADD(Node... nodes){
        FunctionNode functionNode = new FunctionNode ("ADD", Arrays.asList (nodes[0], nodes[1]));
        for ( int i = 2; i < nodes.length; i++ ) {
            functionNode = new FunctionNode ("ADD", Arrays.asList (functionNode, nodes[i]));
        }
        return functionNode;
    }

    public Node MULT(Node... nodes){
        FunctionNode functionNode = new FunctionNode ("MULT", Arrays.asList (nodes[0], nodes[1]));
        for ( int i = 2; i < nodes.length; i++ ) {
            functionNode = new FunctionNode ("MULT", Arrays.asList (functionNode, nodes[i]));
        }
        return functionNode;
    }

    public Node DIV(Node node1, Node node2){
        return new FunctionNode ("DIV", Arrays.asList (node1, node2));
    }

    public Node DIV(Node node, double value){
        return new FunctionNode ("DIV", node, value);
    }

    public Node SUB(Node node1, Node node2){
        return new FunctionNode ("SUB", Arrays.asList (node1, node2));
    }

    public Node HIGHEST(Node input, int period){
        return new FunctionNode ("MAX", input, period);
    }

    public Node LOWEST(Node input, int period){
        return new FunctionNode ("MIN", input, period);
    }

    public Node X(Node input, int size){
        return new FunctionNode ("X", input, size);
    }

    //OPTIMIZATION FUNCTIONS

    public AlgoCreator (Double[] optimizedValueList) {
        if (optimizedValueList != null) {
            this.optimizedValueList = Arrays.asList (optimizedValueList);
        }else{
            this.optimizedValueList = Collections.EMPTY_LIST;
        }
    }

    public void OPT(Double[] values){
        this.optimizedValueList = Arrays.asList (values);
    }

    public AlgoCreator () {
        this.optimizedValueList = Collections.EMPTY_LIST;
    }

    public double Optimize(double start, double end, double step) throws OutOfRangeException {
        if (currentIndex < optimizedValueList.size ()){
            return optimizedValueList.get (currentIndex++);
        }
        if (!optimizedValueList.isEmpty ()) {
            throw new OutOfRangeException (currentIndex, 0, optimizedValueList.size () - 1);
        }
        else{
            optimizerDefList.add (new OptimizerDef (start, end, step));
            return 0;
        }
    }

    public List<OptimizerDef> getOptimizerDefList () {
        return new ArrayList<OptimizerDef> (optimizerDefList);
    }

}