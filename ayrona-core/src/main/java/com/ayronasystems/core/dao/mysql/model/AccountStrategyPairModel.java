package com.ayronasystems.core.dao.mysql.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by gorkemgok on 26/12/15.
 */

@Entity
@Table(name="account_strategy_pair", uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "strategy_id"}))
public class AccountStrategyPairModel extends BaseModel{

    private int accountId;

    private int strategyId;

    private double lot;

    @Column(name = "account_id", nullable = false)
    public int getAccountId () {
        return accountId;
    }

    public void setAccountId (int accountId) {
        this.accountId = accountId;
    }

    @Column(name = "strategy_id", nullable = false)
    public int getStrategyId () {
        return strategyId;
    }

    public void setStrategyId (int strategyId) {
        this.strategyId = strategyId;
    }

    public double getLot () {
        return lot;
    }

    public void setLot (double lot) {
        this.lot = lot;
    }
}
