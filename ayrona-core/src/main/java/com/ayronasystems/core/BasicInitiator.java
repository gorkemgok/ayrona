package com.ayronasystems.core;

import com.ayronasystems.core.strategy.Initiator;

/**
 * Created by gorkemgok on 18/06/16.
 */
public class BasicInitiator implements Initiator{

    private String id;

    private String name;

    public BasicInitiator (String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId () {
        return id;
    }

    public String getName () {
        return name;
    }

    public boolean isSameInitiator (Initiator initiator) {
        return id.equals (initiator.getId ());
    }

    @Override
    public String toString () {
        return name;
    }
}
