package com.ayronasystems.core.edr;

import com.ayronasystems.core.concurrent.QueueRunner;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class EdrQueue {

    private static EdrQueue edrQueue = new EdrQueue();

    private EdrLogger edrLogger;

    private QueueRunner<Edr, EdrLogger> queue;

    public static EdrQueue getQueue(){
        return edrQueue;
    }

    private EdrQueue() {
        edrLogger = EdrLogger.getLogger();
        queue = new QueueRunner<Edr, EdrLogger>(edrLogger);
        queue.start();
    }

    public void putQueue(Edr edr){
        queue.put(edr);
    }

    public boolean isIdle(){
        return queue.isIdle();
    }
}
