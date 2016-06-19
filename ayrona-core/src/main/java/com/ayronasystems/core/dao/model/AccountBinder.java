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

    private double lot;

    public AccountBinder (String id, State state, double lot) {
        this.id = id;
        this.state = state;
        this.lot = lot;
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

    public double getLot () {
        return lot;
    }

    public void setLot (double lot) {
        this.lot = lot;
    }
}
