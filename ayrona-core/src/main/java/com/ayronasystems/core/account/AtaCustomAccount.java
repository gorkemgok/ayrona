package com.ayronasystems.core.account;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class AtaCustomAccount extends BasicAccount{

    private String accountNo;

    public AtaCustomAccount (String id, String accountNo) {
        super (id);
        this.accountNo = accountNo;
    }
}
