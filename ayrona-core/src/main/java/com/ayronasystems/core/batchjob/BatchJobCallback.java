package com.ayronasystems.core.batchjob;

/**
 * Created by gorkemgok on 04/06/16.
 */
public interface BatchJobCallback {

    void complete(BatchJobResult batchJobResult);

    void error(BatchJobResult batchJobResult);

    void update(BatchJob batchJob, int progress);
}
