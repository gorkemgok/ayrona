package com.ayronasystems.core.algo;

import com.ayronasystems.core.*;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.data.GrowingStrategyOHLC;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.SlidingStrategyOHLC;
import com.ayronasystems.core.data.StrategyOHLC;
import com.ayronasystems.core.definition.*;
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
public class AlgoStrategy implements SPStrategy<Bar> {

    private String id;

    private Algo algo;

    private OrderGenerator orderGenerator;

    private OrderHandler orderHandler;

    private Account dummyAccount;

    private double takeProfitRatio;

    private double stopLossRatio;

    private List<AccountBindInfo> accountBindInfoList;

    private StrategyOHLC ohlc;

    private ExecutorService executor;

    private SymbolPeriod symbolPeriod;

    public AlgoStrategy (String id, Algo algo, MarketData initialMarketData, List<AccountBindInfo> accountBindInfoList, double takeProfitRatio, double stopLossRatio) {
        this(true, id, algo, initialMarketData, accountBindInfoList, takeProfitRatio, stopLossRatio);
    }

    public AlgoStrategy (boolean slidingData, String id, Algo algo, MarketData initialMarketData, List<AccountBindInfo> accountBindInfoList, double takeProfitRatio, double stopLossRatio) {
        this.id = id;
        this.algo = algo;
        this.accountBindInfoList = accountBindInfoList;
        this.takeProfitRatio = takeProfitRatio;
        this.stopLossRatio = stopLossRatio;
        this.symbolPeriod = new SymbolPeriod (initialMarketData.getSymbol (), initialMarketData.getPeriod ());

        orderGenerator = new BasicOrderGenerator ();
        executor = Executors.newFixedThreadPool(50);

        orderHandler = new BasicOrderHandler ();
        dummyAccount = new BasicAccount ();

        try {
            if (slidingData){
                ohlc = SlidingStrategyOHLC.valueOf (initialMarketData);
            }else {
                ohlc = GrowingStrategyOHLC.valueOf (initialMarketData);
            }
            ohlc.prepareForNextData ();

            int nic = algo.getNeededInputCount ();
            if (nic <= ohlc.size ()) {
                List<Signal> signalList = algo.getSignalList (ohlc);
                orderGenerator.process (ohlc, signalList);
            }
        } catch ( CorruptedMarketDataException e ) {
            assert(false);
        }
    }

    public void process (Bar bar) throws PrerequisiteException {
        ohlc.overwriteLastBar (bar);

        double[] tpsl = calcuateTPandSL ();
        double takeProfit = tpsl[0];
        double stopLoss =  tpsl[1];

        int neededInputCount = algo.getNeededInputCount ();
        if (neededInputCount <= ohlc.size ()) {
            List<Signal> signalList = algo.getSignalList (ohlc);
            if ( signalList.size () > 0 ) {
                signalList = signalList.subList (signalList.size () - 1, signalList.size ());
            }
            List<Order> orderList = orderGenerator.process (ohlc, signalList);
            orderHandler.process (orderList, this, dummyAccount, 1, takeProfit, stopLoss);
            for ( AccountBindInfo accountBindInfo : accountBindInfoList ) {
                RunnableOrderHandler runnableOrderHandler = new RunnableOrderHandler (orderList,
                                                                                      this,
                                                                                      accountBindInfo,
                                                                                      takeProfit,
                                                                                      stopLoss
                );
                executor.submit (runnableOrderHandler);
            }
        }
        ohlc.prepareForNextData ();
    }

    public SymbolPeriod getSymbolPeriod () {
        return symbolPeriod;
    }

    public List<AccountBindInfo> getAccountBindInfoList () {
        return accountBindInfoList;
    }

    public String getId () {
        return id;
    }

    public String getName () {
        return algo.getName ();
    }

    public boolean isSameInitiator (Initiator initiator) {
        return initiator.getId ().equals (id);
    }

    private double[] calcuateTPandSL(){
        double[] mdClose = ohlc.getPrice (PriceColumn.CLOSE);
        double currentPrice = mdClose[mdClose.length - 1];
        double takeProfit = currentPrice + (currentPrice * takeProfitRatio);
        double stopLoss = currentPrice - (currentPrice * stopLossRatio);
        return new double[]{takeProfit, stopLoss};
    }
}
