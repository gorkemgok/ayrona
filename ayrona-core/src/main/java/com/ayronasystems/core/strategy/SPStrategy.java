package com.ayronasystems.core.strategy;

import com.ayronasystems.core.definition.SymbolPeriod;

/**
 * Created by gorkemgok on 11/06/16.
 */
public interface SPStrategy<E> extends Strategy<E> {

    SymbolPeriod getSymbolPeriod();

}
