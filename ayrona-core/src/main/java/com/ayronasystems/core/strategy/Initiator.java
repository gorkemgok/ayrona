package com.ayronasystems.core.strategy;

/**
 * Created by gorkemgok on 12/05/16.
 */
public interface Initiator {

    String getId ();

    String getName();

    boolean isSameInitiator(Initiator initiator);

}
