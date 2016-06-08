package com.ayronasystems.core.concurrent;

/**
 * Created by gorkemgok on 08/06/16.
 */
public interface QueueRunnable<E> {

    public void process(E element);
}
