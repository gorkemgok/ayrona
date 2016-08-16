package com.ayronasystems.core.strategy;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Signal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 14/05/16.
 */
public class BasicOrderGenerator implements OrderGenerator {

    private Order lastOrder = null;

    private Signal lastSignal;

    public BasicOrderGenerator () {
        lastSignal = Signal.HOLD;
    }

    public BasicOrderGenerator (Order lastOrder) {
        this.lastOrder = lastOrder;
        this.lastSignal = lastOrder.getDirection () == Direction.LONG ? Signal.BUY : Signal.SELL;
    }

    public List<Order> process (MarketData marketData, List<Signal> signalList) {
        List<Order> orderList = new ArrayList<Order> ();
        int j = marketData.size () - signalList.size ();
        for ( int i = 0; i < signalList.size (); i++ ) {
            Signal signal = signalList.get (i);
            Direction direction = signal == Signal.BUY ? Direction.LONG : Direction.SHORT;
            if (signal != lastSignal && (signal == Signal.BUY || signal == Signal.SELL) ){
                Order positionOpenOrder = Order.builder ()
                                               .order (Order.Type.OPEN)
                                               .direction (direction)
                                               .date (marketData.getDate (j))
                                               .price (marketData.getPrice (PriceColumn.CLOSE, j))
                                               .symbol (marketData.getSymbol ())
                                               .description ("Fatih Otomatik Algo")
                                               .build ();
                if ( lastOrder != null && lastOrder.getDirection () != direction){
                    Order positionCloseOrder = positionOpenOrder.inverse ();
                    orderList.add (positionCloseOrder);
                }
                orderList.add (positionOpenOrder);
                lastOrder = positionOpenOrder;
                lastSignal = signal;
            }
            j++;
        }
        return orderList;
    }
}
