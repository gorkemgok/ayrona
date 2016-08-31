package com.ayronasystems.core.backtest;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.strategy.Position;

import java.util.List;

/**
 * Created by gorkemgok on 28/08/16.
 */
public interface BackTestCalculator {

    BackTestResult calculate(List<Position> positionList, MarketData marketData);

}
