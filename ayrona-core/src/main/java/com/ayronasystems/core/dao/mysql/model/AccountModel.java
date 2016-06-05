package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.definition.AccountType;

import javax.persistence.*;

/**
 * Created by gorkemgok on 10/12/15.
 */

@Entity
@Table(name="account", uniqueConstraints = @UniqueConstraint(columnNames = {"broker", "login"}))
public class AccountModel extends BaseModel{

    private String broker;

    private String login;

    private String password;

    private AccountType accountType;

    private UserModel user;

    public String getBroker () {
        return broker;
    }

    public void setBroker (String broker) {
        this.broker = broker;
    }

    public String getLogin () {
        return login;
    }

    public void setLogin (String login) {
        this.login = login;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    public AccountType getAccountType () {
        return accountType;
    }

    public void setAccountType (AccountType accountType) {
        this.accountType = accountType;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }
}
