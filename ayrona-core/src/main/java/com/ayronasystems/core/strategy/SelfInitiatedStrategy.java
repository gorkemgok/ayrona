package com.ayronasystems.core.strategy;

import com.ayronasystems.core.account.AccountBinder;
import com.ayronasystems.core.account.AccountStrategyPair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 12/05/16.
 */
public class SelfInitiatedStrategy implements Strategy<Recommendation>{

    public void process (Recommendation recommendation) {
        List<AccountStrategyPair> accountStrategyPairs = new ArrayList<AccountStrategyPair> ();

        for ( AccountStrategyPair accountStrategyPair : accountStrategyPairs ){
            Position position = Position.builder (this)
                    .description ("SelfInitiatedStrategy")
                    .lot (accountStrategyPair.getLot ())
                    .symbol (recommendation.getSymbol ())
                    .openPrice (recommendation.getPrice ())
                    .direction (recommendation.getDirection ())
                    .openDate (new Date ())
                    .stopLoss (recommendation.getStopLoss ())
                    .takeProfit (recommendation.getTakeProfit ())
                    .build ();
        }
    }

    public List<AccountBinder> getAccountBinderList () {
        return null;
    }

    public void registerAccount (AccountBinder accountBinder) {

    }

    public void deregisterAccount (String accountId) {

    }

    public String getId () {
        return "SIS";
    }

    public String getName () {
        return "Self Initiated Strategy";
    }

    public boolean isSameInitiator (Initiator initiator) {
        return initiator.getId ().equals ("SIS");
    }
}
