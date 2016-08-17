package com.ayronasystems.data.listener;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.core.util.calendar.MarketCalendarService;
import com.ayronasystems.data.Barifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class BasicTickListener implements TickListener{

    private static Logger log = LoggerFactory.getLogger (BasicTickListener.class);

    private volatile List<Barifier> barifierList = new ArrayList<Barifier> ();

    public synchronized void addBarifier(Barifier barifier){
        barifierList.add (barifier);
    }

    public synchronized List<Barifier> getBarifierList (){
        return new ArrayList<Barifier> (barifierList);
    }

    private MarketCalendarService mds = Singletons.INSTANCE.getMarketCalendarService ();

    public void newTick (Tick tick) {
        if (mds.isMarketOpen (tick.getSymbol (), tick.getDate ())) {
            for ( Barifier barifier : getBarifierList () ) {
                barifier.newTick (tick);
            }
        }else{
            log.debug ("Dirty market data {}", tick);
        }
    }
}
