package com.ayronasystems.core.algo;

import com.ayronasystems.core.*;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.data.GrowingStrategyOHLC;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.StrategyOHLC;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.*;
import com.ayronasystems.core.strategy.concurrent.RunnableOrderHandler;
import com.ayronasystems.core.timeseries.moment.Bar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gorkemgok on 12/05/16.
 */
public class AlgoStrategy implements Strategy<Bar> {

    private String id;

    private SignalGenerator signalGenerator;

    private OrderGenerator orderGenerator;

    private double takeProfitRatio;

    private double stopLossRatio;

    private List<AccountBindInfo> accountBindInfoList;

    private StrategyOHLC ohlc;

    private ExecutorService executor;

    //TODO : strategy definition: name in addition to id
    public AlgoStrategy (String id, SignalGenerator signalGenerator, MarketData marketData, List<AccountBindInfo> accountBindInfoList, double takeProfitRatio, double stopLossRatio) {
        this.id = id;
        this.signalGenerator = signalGenerator;
        this.accountBindInfoList = accountBindInfoList;
        this.takeProfitRatio = takeProfitRatio;
        this.stopLossRatio = stopLossRatio;

        orderGenerator = new BasicOrderGenerator ();
        executor = Executors.newFixedThreadPool(50);

        try {
            ohlc = GrowingStrategyOHLC.valueOf (marketData.subData (signalGenerator.getNeededInputCount ()));
        } catch ( CorruptedMarketDataException e ) {
            assert(false);
        }
    }

    public void process (Bar bar) throws PrerequisiteException {
        ohlc.overwriteLastBar (bar);
        double[] mdClose = ohlc.getData (PriceColumn.CLOSE);
        double currentPrice = mdClose[mdClose.length - 1];
        double takeProfit = currentPrice + (currentPrice * takeProfitRatio);
        double stopLoss = currentPrice - (currentPrice * stopLossRatio);

        List<Signal> signalList = signalGenerator.getSignalList (ohlc);
        List<Order> orderList = orderGenerator.process (ohlc, signalList);
        for (AccountBindInfo accountBindInfo : accountBindInfoList) {
            RunnableOrderHandler runnableOrderHandler = new RunnableOrderHandler(orderList,
                                  this,
                                  accountBindInfo,
                                  takeProfit,
                                  stopLoss
            );
            executor.submit(runnableOrderHandler);
        }
        ohlc.prepareForNextData ();
    }

    public List<AccountBindInfo> getAccountBindInfoList () {
        return accountBindInfoList;
    }

    public String getIdentifier () {
        return id;
    }

    public boolean isSameInitiator (Initiator initiator) {
        return initiator.getIdentifier ().equals (id);
    }
}
