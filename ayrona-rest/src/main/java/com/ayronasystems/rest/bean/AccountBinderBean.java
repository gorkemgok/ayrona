package com.ayronasystems.rest.bean;

import com.ayronasystems.core.dao.model.AccountBinder;

/**
 * Created by gorkemgok on 06/06/16.
 */
public class AccountBinderBean {

    private String id;

    private String state;

    private double lot;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getState () {
        return state;
    }

    public void setState (String state) {
        this.state = state;
    }

    public double getLot () {
        return lot;
    }

    public void setLot (double lot) {
        this.lot = lot;
    }

    public AccountBinder toAccountBinder(){
        AccountBinder accountBinder = new AccountBinder ();
        accountBinder.setId (id);
        accountBinder.setLot (lot);
        accountBinder.setState (AccountBinder.State.valueOf (state));
        return accountBinder;
    }
}
