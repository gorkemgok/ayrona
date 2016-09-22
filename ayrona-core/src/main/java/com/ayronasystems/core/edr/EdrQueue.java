package com.ayronasystems.core.edr;

import com.ayronasystems.core.concurrent.QueueRunner;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class EdrQueue {

    private static EdrQueue edrQueue = new EdrQueue();

    private EdrLogger edrLogger;

    private QueueRunner<EdrRecord, EdrLogger> queue;

    public static EdrQueue getQueue(){
        return edrQueue;
    }

    private EdrQueue() {
        edrLogger = EdrLogger.getLogger();
        queue = new QueueRunner<EdrRecord, EdrLogger>(edrLogger);
        queue.start();
    }

    public void putQueue(EdrRecord edrRecord){
        queue.put(edrRecord);
    }

    public boolean isIdle(){
        return queue.isIdle();
    }
}
