package com.ayronasystems.core.service.discovery;

/**
 * Created by gorkemgok on 04/06/16.
 */
public enum ServiceType {
    AY_DS("ayrona-ds"),
    AY_REST("ayrona-rs"),
    MONGODB("mongodb")
    ;

    private String name;

    ServiceType (String name) {
        this.name = name;
    }

    public String getName () {
        return name;
    }
}
