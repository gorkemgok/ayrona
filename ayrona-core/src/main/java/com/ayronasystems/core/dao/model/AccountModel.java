package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Entity;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Entity("account")
public class AccountModel extends BaseModel{

    public enum Type{
        MT4,
        ATA_CUSTOM
    }

    private String accountantName;

    private Type type;

    private LoginDetail loginDetail;

    public String getAccountantName () {
        return accountantName;
    }

    public void setAccountantName (String accountantName) {
        this.accountantName = accountantName;
    }

    public Type getType () {
        return type;
    }

    public void setType (Type type) {
        this.type = type;
    }

    public LoginDetail getLoginDetail () {
        return loginDetail;
    }

    public void setLoginDetail (LoginDetail loginDetail) {
        this.loginDetail = loginDetail;
    }
}
