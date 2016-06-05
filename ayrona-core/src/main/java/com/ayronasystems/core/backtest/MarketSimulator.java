package com.ayronasystems.core.backtest;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.AlgoStrategy;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.StrategyOHLC;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.SignalGenerator;
import com.ayronasystems.core.strategy.Strategy;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;

import java.util.Arrays;

/**
 * Created by gorkemgok on 29/05/16.
 */
public class MarketSimulator {

    private Strategy<Bar> strategy;

    private MarketData marketData;

    private int neededInputCount;

    public MarketSimulator (String code, MarketData marketData) {
        AccountBindInfo accountBindInfo = new AccountBindInfo (new BasicAccount (), 1);
        SignalGenerator signalGenerator = Algo.createInstance (code);
        neededInputCount = signalGenerator.getNeededInputCount ();
        MarketData initialData = marketData.subData (0, neededInputCount - 1);
        strategy = new AlgoStrategy ("0", signalGenerator , initialData, Arrays.asList (accountBindInfo), 0, 0);
        this.marketData = marketData;
    }

    public Account simulate(){
        MarketData simulatedMarketData = marketData.subData (neededInputCount - 1, marketData.getDataCount () - 1);
        int i = 0;
        for (Moment moment : simulatedMarketData) {
           Bar bar = (Bar)moment;
            try {
                strategy.process (bar);
            } catch ( PrerequisiteException e ) {
                e.printStackTrace ();
            }
        }
        return strategy.getAccountBindInfoList ().get (0).getAccount ();
    }
}
