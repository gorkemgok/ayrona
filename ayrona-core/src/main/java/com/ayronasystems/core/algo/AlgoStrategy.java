package com.ayronasystems.core.algo;

import com.ayronasystems.core.*;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.AccountStrategyPair;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.StrategyOHLC;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.*;
import com.ayronasystems.core.timeseries.moment.Bar;

import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public class AlgoStrategy implements Strategy<Bar> {

    private String id;

    private SignalGenerator signalGenerator;

    private OrderGenerator orderGenerator;

    private OrderHandler orderHandler;

    private double takeProfitRatio;

    private double stopLossRatio;

    private List<AccountBindInfo> accountBindInfoList;

    private StrategyOHLC ohlc;

    public AlgoStrategy (String id, SignalGenerator signalGenerator, MarketData marketData, List<AccountBindInfo> accountBindInfoList, double takeProfitRatio, double stopLossRatio) {
        this.id = id;
        this.signalGenerator = signalGenerator;
        this.accountBindInfoList = accountBindInfoList;
        this.takeProfitRatio = takeProfitRatio;
        this.stopLossRatio = stopLossRatio;

        orderGenerator = new BasicOrderGenerator ();
        orderHandler = new BasicOrderHandler ();

        try {
            ohlc = StrategyOHLC.valueOf (marketData.subData (signalGenerator.getNeededInputCount ()));
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

        //long a = System.nanoTime ();
        List<Signal> signalList = signalGenerator.getSignalList (ohlc);
        //long b = System.nanoTime ();
        List<Order> orderList = orderGenerator.process (ohlc, signalList);
        //long c = System.nanoTime ();
        for (AccountBindInfo accountBindInfo : accountBindInfoList) {
            orderHandler.process (orderList,
                                  this,
                                  accountBindInfo.getAccount (),
                                  accountBindInfo.getLot (),
                                  takeProfit,
                                  stopLoss
            );

        }
        //long d = System.nanoTime ();
        //System.out.println ("\t-"+"a"+(b-a)+"ns, "+"b"+(c-b)+"ns, "+"c"+(d-c)+"ns");
        ohlc.slideSeries ();
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
