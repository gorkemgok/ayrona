package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by gorkemgok on 06/06/16.
 */
@Embedded
public class AccountBinder {

    public enum State{
        ACTIVE,
        INACTIVE
    }

    private String id;

    private State state;

    public AccountBinder (String id, State state) {
        this.id = id;
        this.state = state;
    }

    public AccountBinder () {
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public State getState () {
        return state;
    }

    public void setState (State state) {
        this.state = state;
    }
}
