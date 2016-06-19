package com.ayronasystems.ate;

import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.dao.model.StrategyModel;

import java.util.List;

/**
 * Created by gorkemgok on 19/06/16.
 */
public class RunningStrategy {

    private StrategyModel strategyModel;

    private List<AccountBindInfo> accountBindInfoList;

    public RunningStrategy (StrategyModel strategyModel, List<AccountBindInfo> accountBindInfoList) {
        this.strategyModel = strategyModel;
        this.accountBindInfoList = accountBindInfoList;
    }

    public StrategyModel getStrategyModel () {
        return strategyModel;
    }

    public List<AccountBindInfo> getAccountBindInfoList () {
        return accountBindInfoList;
    }
}
