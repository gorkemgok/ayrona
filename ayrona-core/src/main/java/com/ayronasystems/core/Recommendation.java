package com.ayronasystems.core;

import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;

/**
 * Created by gorkemgok on 12/05/16.
 */
public class Recommendation {

    private Symbol symbol;

    private Direction direction;

    private double price;

    private double takeProfit;

    private double stopLoss;

    public Recommendation (Symbol symbol, Direction direction, double price, double takeProfit, double stopLoss) {
        this.symbol = symbol;
        this.direction = direction;
        this.price = price;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public double getPrice () {
        return price;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public Direction getDirection () {
        return direction;
    }

    public double getTakeProfit () {
        return takeProfit;
    }

    public double getStopLoss () {
        return stopLoss;
    }
}
