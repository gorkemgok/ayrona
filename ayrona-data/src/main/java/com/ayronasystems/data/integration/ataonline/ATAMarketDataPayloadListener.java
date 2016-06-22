package com.ayronasystems.data.integration.ataonline;

import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.data.listener.BasicTickListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class ATAMarketDataPayloadListener {

    private static Logger log = LoggerFactory.getLogger (ATAMarketDataPayloadListener.class);

    private BasicTickListener tickListener;

    public ATAMarketDataPayloadListener (BasicTickListener tickListener) {
        this.tickListener = tickListener;
    }

    public void newPayload(ATAMarketDataPayload marketDataPayload){
        try {
            if (marketDataPayload.getSymbolCode ().equals (ATAMarketDataPayload.VOB_SYMBOL_CODE)) {
                Tick tick = new Tick (
                        ATAMarketDataPayload.SDF.parse (marketDataPayload.getDate ()),
                        Symbol.VOB30,
                        marketDataPayload.getPrice (),
                        marketDataPayload.getPrice (),
                        marketDataPayload.getPrice ()
                );
                tickListener.newTick (tick);
            }
        } catch ( Exception e ) {
            log.error ("Cant convert marketdata payload to tick", e);
        }
    }
}
