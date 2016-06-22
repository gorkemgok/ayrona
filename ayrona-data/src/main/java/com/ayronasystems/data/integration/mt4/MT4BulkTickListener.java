package com.ayronasystems.data.integration.mt4;

import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.data.listener.BasicTickListener;
import com.jfx.MT4;
import com.jfx.Tick;
import com.jfx.strategy.Strategy;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gorkemgok on 10/12/15.
 */
public class MT4BulkTickListener implements Strategy.BulkTickListener{

    private BasicTickListener tickListener;

    public MT4BulkTickListener (BasicTickListener tickListener) {
        this.tickListener = tickListener;
    }

    public void onTicks (List<Tick> list, MT4 mt4) {
        for ( Tick tick : list ) {
            BigDecimal ask = new BigDecimal (String.valueOf (tick.ask));
            BigDecimal bid = new BigDecimal (String.valueOf (tick.bid));
            BigDecimal mid = ask.add (bid)
                                .divide (new BigDecimal (2));
            if (Symbol.hasSymbol (tick.symbol)) {
                System.out.println ("---"+tick.symbol+", "+tick.time);
                com.ayronasystems.core.timeseries.moment.Tick ayTick =
                        new com.ayronasystems.core.timeseries.moment.Tick (
                                tick.time, Symbol.valueOf (tick.symbol), ask.doubleValue (), mid.doubleValue (),
                                bid.doubleValue ()
                        );
                tickListener.newTick (ayTick);
            }
        }
    }
}
