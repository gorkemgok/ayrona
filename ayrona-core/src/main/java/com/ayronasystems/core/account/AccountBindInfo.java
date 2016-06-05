package com.ayronasystems.core.account;

/**
 * Created by gorkemgok on 29/05/16.
 */
public class AccountBindInfo {

    private Account account;

    private double lot;

    public AccountBindInfo (Account account, double lot) {
        this.account = account;
        this.lot = lot;
    }

    public Account getAccount () {
        return account;
    }

    public double getLot () {
        return lot;
    }
}
