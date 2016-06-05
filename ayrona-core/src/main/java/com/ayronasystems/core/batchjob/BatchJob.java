package com.ayronasystems.core.batchjob;

/**
 * Created by gorkemgok on 04/06/16.
 */
public interface BatchJob extends Runnable{

    enum Type{
        IMPORT_MARKETDATA,
        TRAIN_STRATEGY,
        BACKTEST,
        ANALYZE_MARKETDATA
        ;
    }

    enum Status{
        RUNNING,
        COMPLETED,
        FAILED,
        WAITING,
        CANCELED;
    }

    Type getType();

    Status getStatus();

    void cancel();

    void setCallback(BatchJobCallback batchJobCallback);

    String getId();

    void setId(String id);
}
