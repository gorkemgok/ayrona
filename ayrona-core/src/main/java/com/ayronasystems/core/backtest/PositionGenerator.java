package com.ayronasystems.core.backtest;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.*;

import java.util.List;

/**
 * Created by gorkemgok on 18/05/16.
 */
public class PositionGenerator {

    private SignalGenerator signalGenerator;

    private OrderGenerator orderGenerator = new BasicOrderGenerator ();

    private OrderHandler orderHandler = new BasicOrderHandler ();

    private Account account = new BackTestAccount ();

    public PositionGenerator (SignalGenerator signalGenerator) {
        this.signalGenerator = signalGenerator;
    }

    public List<Position> generate (MarketData marketData) throws PrerequisiteException {
        List<Signal> signalList = signalGenerator.getSignalList (marketData);
        List<Order> orderList = orderGenerator.process (marketData, signalList);
        return orderHandler.process (orderList, BackTestInitiator.INSTANCE, account, 1, 0, 0);
    }
}
