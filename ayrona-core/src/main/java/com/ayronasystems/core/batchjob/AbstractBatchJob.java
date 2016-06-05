package com.ayronasystems.core.batchjob;

/**
 * Created by gorkemgok on 04/06/16.
 */
public abstract class AbstractBatchJob implements BatchJob{

    private String id;

    protected Status status;

    protected BatchJobCallback batchJobCallback;

    protected boolean doContinue = true;

    public Status getStatus () {
        return status;
    }

    public void cancel () {
        doContinue = false;
    }

    public void setCallback (BatchJobCallback batchJobCallback) {
        this.batchJobCallback = batchJobCallback;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getId () {
        return id;
    }
}
