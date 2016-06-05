package com.ayronasystems.core.dao.mysql.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 30/12/15.
 */
@Entity
@Table(name = "user")
public class UserModel extends BaseModel {

    public enum UserRole{
        ADMIN,
        BROKER,
        INVESTOR;
    }

    private String userName;

    private String password;

    private UserRole userRole;

    private List<AccountModel> boundAccounts = new ArrayList<AccountModel> ();

    @Column(name = "user_name", nullable = false)
    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    @Column(name = "password", nullable = false)
    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    public UserRole getUserRole () {
        return userRole;
    }

    public void setUserRole (UserRole userRole) {
        this.userRole = userRole;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public List<AccountModel> getBoundAccounts () {
        return boundAccounts;
    }

    public void setBoundAccounts (List<AccountModel> boundAccounts) {
        this.boundAccounts = boundAccounts;
    }
}
