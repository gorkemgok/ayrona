package com.ayronasystems.core.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by gorkemgok on 08/06/16.
 */
public class QueueRunner<E, R extends QueueRunnable<E>> implements Runnable{

    private R runnable;

    private Thread thread;

    private volatile ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E> ();

    private volatile boolean isStopped;

    private Object lock = new Object ();

    public QueueRunner (R runnable) {
        this.runnable = runnable;
        this.isStopped = false;
    }

    public void put(E element){
        queue.add (element);
        resume ();
    }

    public synchronized void stop(){
        isStopped = true;
        resume ();
    }

    public void start(){
        thread = new Thread (this);
        thread.start ();
    }

    public boolean isIdle(){
        return queue.isEmpty();
    }

    public void run () {
        try {
            while ( !isStopped ) {
                synchronized (lock) {
                    while ( !queue.isEmpty () ) {
                        E element = queue.peek ();
                        if ( element != null ) {
                            runnable.process (element);
                        }
                        queue.remove();
                    }
                    lock.wait ();
                }
            }
        } catch ( InterruptedException e1 ) {
            e1.printStackTrace ();
        }
    }

    public R unwrapRunnable(){
        return runnable;
    }

    public Thread unwrapThread(){
        return thread;
    }

    private void resume(){
        if (thread.getState () == Thread.State.WAITING){
            synchronized (lock){
                if (thread.getState () == Thread.State.WAITING){
                    lock.notify ();
                }
            }
        }
    }

}
