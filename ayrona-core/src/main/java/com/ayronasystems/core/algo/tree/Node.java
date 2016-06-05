package com.ayronasystems.core.algo.tree;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.algo.FIOExchange;

/**
 * Created by gorkemgok on 19/03/16.
 */
public interface Node {

    void checkPrerequisite (boolean checkPrerequisite);

    int getNeededInputCount ();

    FIOExchange calculate (MarketData marketData) throws PrerequisiteException;
}
