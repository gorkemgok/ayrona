package com.ayronasystems.core.batchjob;

/**
 * Created by gorkemgok on 04/06/16.
 */
public class BatchJobResult<T> {

    private BatchJob batchJob;

    private T t;

    public BatchJobResult (BatchJob batchJob, T t) {
        this.batchJob = batchJob;
        this.t = t;
    }

    public BatchJob getBatchJob () {
        return batchJob;
    }

    public T getT () {
        return t;
    }
}
