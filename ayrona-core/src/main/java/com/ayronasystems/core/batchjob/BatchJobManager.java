package com.ayronasystems.core.batchjob;

import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.BatchJobModel;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by gorkemgok on 04/06/16.
 */
public class BatchJobManager {

    public static class Callback implements BatchJobCallback{

        private Dao dao;

        public Callback (Dao dao) {
            this.dao = dao;
        }

        public void complete (BatchJobResult batchJobResult) {
            dao.updateBatchJob (batchJobResult.getBatchJob ().getId (), BatchJob.Status.COMPLETED);
        }

        public void error (BatchJobResult batchJobResult) {
            dao.updateBatchJob (batchJobResult.getBatchJob ().getId (), BatchJob.Status.FAILED);
        }

        public void update (BatchJob batchJob, int progress) {
            dao.updateBatchJob (batchJob.getId (), progress);
        }
    }

    private ExecutorService executor;

    private BatchJobCallback callback;

    private Dao dao;

    public BatchJobManager (Dao dao, int parallelism) {
        this.dao = dao;
        this.executor = new ForkJoinPool (parallelism);
        this.callback = new Callback (dao);
    }

    public String start(BatchJob job){
        BatchJobModel batchJobModel = new BatchJobModel ();
        batchJobModel.setType (job.getType ());
        batchJobModel.setStatus (BatchJob.Status.WAITING);
        batchJobModel.setProgress (0);
        batchJobModel.setStartDate (new Date ());
        dao.createBatchJob (batchJobModel);
        job.setCallback (callback);
        job.setId (batchJobModel.getId ());
        executor.execute (job);
        return batchJobModel.getId ();
    }
}
