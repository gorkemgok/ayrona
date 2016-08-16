package com.ayronasystems.core.strategy;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Signal;

import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface OrderGenerator {

    List<Order> process(MarketData marketData, List<Signal> signals);

}
