package com.ayronasystems.rest.bean;

import com.ayronasystems.core.dao.model.AccountBinderModel;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gorkemgok on 20/06/16.
 */
@XmlRootElement
public class BoundStrategyBean {

    private AccountBinderModel.State state;

    private double lot;

    private StrategyBean strategy;

    public BoundStrategyBean (AccountBinderModel.State state, double lot, StrategyBean strategy) {
        this.state = state;
        this.lot = lot;
        this.strategy = strategy;
    }

    public AccountBinderModel.State getState () {
        return state;
    }

    public void setState (AccountBinderModel.State state) {
        this.state = state;
    }

    public StrategyBean getStrategy () {
        return strategy;
    }

    public void setStrategy (StrategyBean strategy) {
        this.strategy = strategy;
    }

    public double getLot () {
        return lot;
    }

    public void setLot (double lot) {
        this.lot = lot;
    }
}
