package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Entity;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Entity("user")
public class UserModel extends BaseModel{

    private String name;

    private String login;

    private String password;

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

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
