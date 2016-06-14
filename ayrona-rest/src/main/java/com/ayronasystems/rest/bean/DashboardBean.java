package com.ayronasystems.rest.bean;

import com.ayronasystems.core.dao.model.StrategyStat;

/**
 * Created by gorkemgok on 14/06/16.
 */
public class DashboardBean {

    private StrategyStat strategyStat;

    private long accountCount;

    public DashboardBean (StrategyStat strategyStat, long accountCount) {
        this.strategyStat = strategyStat;
        this.accountCount = accountCount;
    }

    public StrategyStat getStrategyStat () {
        return strategyStat;
    }

    public void setStrategyStat (StrategyStat strategyStat) {
        this.strategyStat = strategyStat;
    }

    public long getAccountCount () {
        return accountCount;
    }

    public void setAccountCount (long accountCount) {
        this.accountCount = accountCount;
    }
}
