package com.ayronasystems.ate.rest.bean;

import com.ayronasystems.ate.RunningStrategy;
import com.ayronasystems.core.account.AccountBindInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 19/06/16.
 */
public class RunningStrategyBean {

    private String Id;

    private String name;

    private String symbol;

    private String period;

    private List<RunningAccountBean> boundAccounts;

    public String getId () {
        return Id;
    }

    public void setId (String id) {
        Id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol (String symbol) {
        this.symbol = symbol;
    }

    public String getPeriod () {
        return period;
    }

    public void setPeriod (String period) {
        this.period = period;
    }

    public List<RunningAccountBean> getBoundAccounts () {
        return boundAccounts;
    }

    public void setBoundAccounts (List<RunningAccountBean> boundAccounts) {
        this.boundAccounts = boundAccounts;
    }

    public static RunningStrategyBean valueOf(RunningStrategy runningStrategy){
        RunningStrategyBean bean = new RunningStrategyBean ();
        bean.setId (runningStrategy.getStrategyModel ().getId ());
        bean.setName (runningStrategy.getStrategyModel ().getName ());
        bean.setSymbol (runningStrategy.getStrategyModel ().getSymbol ().toString ());
        bean.setPeriod (runningStrategy.getStrategyModel ().getPeriod ().toString ());
        List<RunningAccountBean> accountBeanList = new ArrayList<RunningAccountBean> ();
        for ( AccountBindInfo accountBindInfo : runningStrategy.getAccountBindInfoList ()){
            RunningAccountBean accountBean = new RunningAccountBean (
                    accountBindInfo.getAccount ().getId (),
                    accountBindInfo.getAccount ().getName (),
                    accountBindInfo.getLot ()
            );
            accountBeanList.add (accountBean);
        }
        bean.setBoundAccounts (accountBeanList);
        return bean;
    }
}
