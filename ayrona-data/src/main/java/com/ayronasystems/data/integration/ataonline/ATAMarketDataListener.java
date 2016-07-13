package com.ayronasystems.data.integration.ataonline;

import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.data.listener.BasicTickListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class ATAMarketDataListener {

    private static Logger log = LoggerFactory.getLogger (ATAMarketDataListener.class);

    private BasicTickListener tickListener;

    public ATAMarketDataListener (BasicTickListener tickListener) {
        this.tickListener = tickListener;
    }

    public void newPayload(ATAMarketDataPayload marketDataPayload){
        try {
            for (Symbol symbol : Symbols.getList ()) {
                if ( marketDataPayload.getSymbolCode ()
                                      .equals (symbol.getCode ()) ) {
                    Tick tick = new Tick (
                            ATAMarketDataPayload.SDF.parse (marketDataPayload.getDate ()),
                            symbol,
                            marketDataPayload.getPrice (),
                            marketDataPayload.getPrice (),
                            marketDataPayload.getPrice ()
                    );
                    tickListener.newTick (tick);
                }
            }
        } catch ( Exception e ) {
            log.error ("Cant convert marketdata payload to tick", e);
        }
    }
}
