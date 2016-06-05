package com.ayronasystems.core;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface Initiator {

    String getIdentifier();

    boolean isSameInitiator(Initiator initiator);

}
