package com.ayronasystems.ate;

import com.ayronasystems.core.account.AccountBinder;
import com.ayronasystems.core.strategy.SPStrategy;

import java.util.List;

/**
 * Created by gorkemgok on 19/06/16.
 */
public class RunningStrategy {

    private SPStrategy strategy;

    private List<AccountBinder> accountBinderList;

    public RunningStrategy (SPStrategy strategy) {
        this.strategy = strategy;
        this.accountBinderList = strategy.getAccountBinderList ();
    }

    public SPStrategy getStrategy () {
        return strategy;
    }

    public List<AccountBinder> getAccountBinderList () {
        return accountBinderList;
    }
}
