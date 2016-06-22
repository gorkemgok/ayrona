package com.ayronasystems.data.listener;

import com.ayronasystems.core.timeseries.moment.Tick;

/**
 * Created by gorkemgok on 21/06/16.
 */
public interface TickListener {

    void newTick(Tick tick);
}
