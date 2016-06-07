package com.ayronasystems.data;

import com.ayronasystems.core.definition.Symbol;
import com.jfx.MT4;
import com.jfx.Tick;
import com.jfx.strategy.Strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 10/12/15.
 */
public class MT4BulkTickListener implements Strategy.BulkTickListener{

    private List<Barifier> barifierList = new ArrayList<Barifier> ();

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
                for ( Barifier barifier : barifierList ) {
                    barifier.newTick (ayTick);
                }
            }
        }
    }

    public void addBarifier(Barifier barifier){
        barifierList.add (barifier);
    }

    public List<Barifier> getBarifierList (){
        return barifierList;
    }
}
