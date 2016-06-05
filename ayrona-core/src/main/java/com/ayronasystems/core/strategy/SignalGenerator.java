package com.ayronasystems.core.strategy;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.exception.PrerequisiteException;

import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface SignalGenerator {

    List<Signal> getSignalList (MarketData marketData) throws PrerequisiteException;

    int getNeededInputCount();
}
