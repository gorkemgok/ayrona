package com.ayronasystems.core.algo.tree;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.algo.Function;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.algo.FIOExchange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class FunctionNode implements Node {

    private static final double[] EMPTY_PARAM = new double[0];

    private boolean checkPrerequisite = false;

    private List<? extends Node> nodes;

    private double[] params;

    private Function function;

    public FunctionNode (String fnName, List<? extends Node> nodes, double[] params) {
        //TODO : Write test
        this.function = FunctionFactory.getInstance (fnName);
        this.nodes = nodes;
        this.params = params;
    }

    public FunctionNode (String fnName, List<? extends Node> nodes) {
        //TODO : Write test
        this.function = FunctionFactory.getInstance (fnName);
        this.nodes = nodes;
        this.params = EMPTY_PARAM;
    }

    public FunctionNode (String fnName, Node node, double[] params) {
        this.function = FunctionFactory.getInstance (fnName);
        this.nodes = Arrays.asList (node);
        this.params = params;
    }

    public FunctionNode (String fnName, Node node, double param) {
        this.function = FunctionFactory.getInstance (fnName);
        this.nodes = Arrays.asList (node);
        this.params = new double[]{param};
    }

    public FunctionNode (String fnName, Node node) {
        this.function = FunctionFactory.getInstance (fnName);
        this.nodes = Arrays.asList (node);
        this.params = EMPTY_PARAM;
    }

    public FIOExchange calculate (MarketData marketData, FIOTable fioTable) throws PrerequisiteException {
        List<FIOExchange> fioExchangeList = new ArrayList<FIOExchange> (nodes.size ());
        for (Node node : nodes){
            node.checkPrerequisite (checkPrerequisite);
            FIOExchange fioExchange = node.calculate (marketData, fioTable);
            fioExchangeList.add (fioExchange);
        }
        FIOExchange fioExchange = new FIOExchange (fioExchangeList);
        if (checkPrerequisite){
            function.checkPrerequisites (fioExchange, params);
        }
        FIOExchange result = function.calculate (fioExchange, params);
        if (fioTable != null){
            fioTable.add (toString (), result);
        }
        return result;
    }

    public void checkPrerequisite (boolean checkPrerequisite) {
        this.checkPrerequisite = checkPrerequisite;
    }

    public int getNeededInputCount () {
        int nnc = 0;
        for (Node node : nodes){
            nnc = Math.max (nnc, node.getNeededInputCount ());
        }
        int fnc = function.getNeededInputCount (params);
        if (nnc == 0){
            return nnc + fnc;
        }else{
            return nnc + fnc - 1;
        }
    }

    public FIOExchange calculate (MarketData marketData) throws PrerequisiteException {
        return calculate (marketData, null);
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder (function.getDefinition ().getName ()).append ("(");
        boolean hasElem = false;
        for ( Node node : nodes ) {
            if (hasElem){
                sb.append (",");
            }
            sb.append (node.toString ());
            hasElem = true;
        }
        for ( double param : params ) {
            if (hasElem){
                sb.append (",");
            }
            sb.append (param);
            hasElem = true;
        }
        sb.append (")");
        return sb.toString ();
    }
}
