package com.ayronasystems.core.algo;

import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.algo.tree.FunctionNode;
import com.ayronasystems.core.algo.tree.MarketDataNode;
import com.ayronasystems.core.algo.tree.Node;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.SignalGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 13/05/16.
 */
public abstract class AlgoModule implements SignalGenerator{

    protected String NAME;

    protected Node BUY;

    protected Node SELL;

    protected final Node O = new MarketDataNode (PriceColumn.OPEN);
    protected final Node H = new MarketDataNode (PriceColumn.HIGH);
    protected final Node L = new MarketDataNode (PriceColumn.LOW);
    protected final Node C = new MarketDataNode (PriceColumn.CLOSE);

    public abstract void define();


    public String getId () {
        return NAME;
    }

    public Algo toAlgo(){
        Algo algo = new Algo (NAME, BUY, SELL);
        return algo;
    }

    public boolean isSameInitiator (Initiator initiator) {
        return initiator.getId ().equals (NAME);
    }

    public List<Signal> getSignalList (MarketData marketData) throws PrerequisiteException {
        BUY.checkPrerequisite (true);
        SELL.checkPrerequisite (true);
        double[] buySignals = BUY.calculate (marketData).getData (0);
        double[] sellSignals = SELL.calculate (marketData).getData (0);
        int minLength = Math.min (buySignals.length, sellSignals.length);
        Signal[] signalList = new Signal[minLength];
        for ( int i = minLength - 1; i > -1; i-- ) {
            if (buySignals[i] == 1 && sellSignals[i] == 0){
                signalList[i] = Signal.BUY;
            }else if (buySignals[i] == 0 && sellSignals[i] == 1){
                signalList[i] = Signal.SELL;
            }else if (buySignals[i] == 0 && sellSignals[i] == 0){
                signalList[i] = Signal.HOLD;
            }else{
                signalList[i] = Signal.AMBIGUOUS;
            }
        }
        return Arrays.asList (signalList);
    }

    public int getNeededInputCount () {
        return Math.max (BUY.getNeededInputCount (), SELL.getNeededInputCount ());
    }

    ;

    public String getName () {
        return NAME;
    }

    protected Node SMA(Node input, int period){
        return new FunctionNode ("SMA", input, new double[]{period});
    }

    protected Node SAR(double acceleration, double maximum){
        return new FunctionNode ("SAR", Arrays.asList (new MarketDataNode (PriceColumn.HIGH), new MarketDataNode(PriceColumn.LOW)), new double[]{acceleration, maximum});
    }

    protected Node AND(Node node1, Node node2){
        return new FunctionNode ("AND", Arrays.asList (node1, node2));
    }

    protected Node OR(Node node1, Node node2){
        return new FunctionNode ("OR", Arrays.asList (node1, node2));
    }

    protected Node LT(Node node1, Node node2){
        return new FunctionNode ("LT", Arrays.asList (node1, node2));
    }

    protected Node GT(Node node1, Node node2){
        return new FunctionNode ("GT", Arrays.asList (node1, node2));
    }

    protected Node LT(Node node, double value){
        return new FunctionNode ("LT", node, new double[]{value});
    }

    protected Node GT(Node node, double value){
        return new FunctionNode ("GT", node, new double[]{value});
    }

    protected Node ADD(Node... nodes){
        FunctionNode functionNode = new FunctionNode ("ADD", Arrays.asList (nodes[0], nodes[1]));
        for ( int i = 2; i < nodes.length; i++ ) {
            functionNode = new FunctionNode ("ADD", Arrays.asList (functionNode, nodes[i]));
        }
        return functionNode;
    }

    protected Node MULT(Node... nodes){
        FunctionNode functionNode = new FunctionNode ("MULT", Arrays.asList (nodes[0], nodes[1]));
        for ( int i = 2; i < nodes.length; i++ ) {
            functionNode = new FunctionNode ("MULT", Arrays.asList (functionNode, nodes[i]));
        }
        return functionNode;
    }

    protected Node DIV(Node node1, Node node2){
        return new FunctionNode ("DIV", Arrays.asList (node1, node2));
    }

    protected Node DIV(Node node, double value){
        return new FunctionNode ("DIV", node, value);
    }

    protected Node SUB(Node node1, Node node2){
        return new FunctionNode ("SUB", Arrays.asList (node1, node2));
    }

    protected Node HIGHEST(Node input, int period){
        return new FunctionNode ("MAX", input, period);
    }

    protected Node LOWEST(Node input, int period){
        return new FunctionNode ("MIN", input, period);
    }

}
