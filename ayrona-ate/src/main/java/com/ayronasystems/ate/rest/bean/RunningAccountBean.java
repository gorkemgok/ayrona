package com.ayronasystems.ate.rest.bean;

/**
 * Created by gorkemgok on 19/06/16.
 */
public class RunningAccountBean {

    private String id;

    private String name;

    private double lot;

    public RunningAccountBean (String id, String name, double lot) {
        this.id = id;
        this.name = name;
        this.lot = lot;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public double getLot () {
        return lot;
    }

    public void setLot (double lot) {
        this.lot = lot;
    }
}
