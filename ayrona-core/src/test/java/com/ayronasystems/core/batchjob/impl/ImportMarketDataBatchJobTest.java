package com.ayronasystems.core.batchjob.impl;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.batchjob.BatchJobManager;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.BatchJobModel;
import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.util.DateUtils;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 05/06/16.
 */
public class ImportMarketDataBatchJobTest {

    public static final String AYRONA_TEST_DB_NAME = "ayrona_test";

    public static final String AYRONA_MARKETDATA_TEST_DB_NAME = "ayrona_marketdata_test";

    private MongoClient mongoClient;

    private Dao dao;

    @Before
    public void setUp () throws Exception {
        mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (AYRONA_TEST_DB_NAME);
        mongoClient.dropDatabase (AYRONA_MARKETDATA_TEST_DB_NAME);

        dao = new MongoDao (Singletons.INSTANCE.getMongoClient (), AYRONA_TEST_DB_NAME);
    }

    @Test
    public void run () throws Exception {
        int i = 0;
        List<Bar> barList = Arrays.asList (
                new Bar (DateUtils.parseDate ("01.01.2017 01:00:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:05:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:10:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:15:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:20:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:25:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:30:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:35:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:40:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:45:00"), i++, i, i, i, i)
        );

        ImportMarketDataBatchJob importMarketDataBatchJob = new ImportMarketDataBatchJob ("test", barList, mongoClient, 2);

        BatchJobModel batchJobModel = new BatchJobModel ();
        batchJobModel.setType (importMarketDataBatchJob.getType ());
        batchJobModel.setStatus (BatchJob.Status.WAITING);
        batchJobModel.setProgress (0);
        batchJobModel.setStartDate (new Date ());
        dao.createBatchJob (batchJobModel);

        importMarketDataBatchJob.setId (batchJobModel.getId ());
        importMarketDataBatchJob.setCallback (new BatchJobManager.Callback(dao));
        importMarketDataBatchJob.run ();
    }

}