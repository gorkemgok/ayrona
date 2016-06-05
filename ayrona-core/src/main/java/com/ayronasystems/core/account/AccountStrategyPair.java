package com.ayronasystems.core.account;

import com.ayronasystems.core.strategy.Strategy;

/**
 * Created by gorkemgok on 12/05/16.
 */
public class AccountStrategyPair {

    private Account account;

    private Strategy strategy;

    private double lot;

    public AccountStrategyPair (Account account, Strategy strategy, double lot) {
        this.account = account;
        this.strategy = strategy;
        this.lot = lot;
    }

    public Account getAccount () {
        return account;
    }

    public Strategy getStrategy () {
        return strategy;
    }

    public double getLot () {
        return lot;
    }
}
