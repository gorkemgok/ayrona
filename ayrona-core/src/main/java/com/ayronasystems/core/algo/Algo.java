package com.ayronasystems.core.algo;

import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.algo.tree.MarketDataNode;
import com.ayronasystems.core.algo.tree.Node;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.SignalGenerator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 25/05/16.
 */
public class Algo implements SignalGenerator, Initiator {

    private String name;

    private Node buy;

    private Node sell;

    protected final Node O = new MarketDataNode (PriceColumn.OPEN);
    protected final Node H = new MarketDataNode (PriceColumn.HIGH);
    protected final Node L = new MarketDataNode (PriceColumn.LOW);
    protected final Node C = new MarketDataNode (PriceColumn.CLOSE);

    public Algo (String name, Node buy, Node sell) {
        this.name = name;
        this.buy = buy;
        this.sell = sell;
    }

    public String getId () {
        return name;
    }

    public boolean isSameInitiator (Initiator initiator) {
        return initiator.getId ().equals (name);
    }

    public List<Signal> getSignalList (MarketData marketData) throws PrerequisiteException {
        buy.checkPrerequisite (true);
        sell.checkPrerequisite (true);
        double[] buySignals = buy.calculate (marketData).getData (0);
        double[] sellSignals = sell.calculate (marketData).getData (0);
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
        return Math.max (buy.getNeededInputCount (), sell.getNeededInputCount ());
    };

    public String getName () {
        return name;
    }

    public static Algo createInstance(String code){
        return createInstance (code, null);
    }

    public static Algo createInstance(String code, String name){
        AlgoCreator algoCreator = new AlgoCreator ();
        Context context = Context.enter ();
        Scriptable scope = context.initStandardObjects ();
        Object jsAlgoCreator = Context.javaToJS (algoCreator, scope);
        ScriptableObject.putProperty (scope, "Sistem", jsAlgoCreator);
        Object result = context.evaluateString(scope, code, "<cmd>", 1, null);
        if (name == null){
            name = algoCreator.NAME;
        }
        return new Algo (name, algoCreator.BUY, algoCreator.SELL);
    }

}
