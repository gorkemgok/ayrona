package com.ayronasystems.data.listener;

import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.data.Barifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class BasicTickListener implements TickListener{

    private volatile List<Barifier> barifierList = new ArrayList<Barifier> ();

    public synchronized void addBarifier(Barifier barifier){
        barifierList.add (barifier);
    }

    public synchronized List<Barifier> getBarifierList (){
        return new ArrayList<Barifier> (barifierList);
    }

    public void newTick (Tick tick) {
        for ( Barifier barifier : getBarifierList () ) {
            barifier.newTick (tick);
        }
    }
}
