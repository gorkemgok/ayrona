package com.ayronasystems.core.batchjob.impl;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.batchjob.BatchJobManager;
import com.ayronasystems.core.configuration.ConfigurationConstants;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.BatchJobModel;
import com.ayronasystems.core.dao.model.MarketDataModel;
import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.core.dao.mongo.MongoDaoTestITCase;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.util.DateUtils;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 05/06/16.
 */
public class ImportMarketDataBatchJobTest {

    private MongoClient mongoClient;

    private Dao dao;

    @Before
    public void setUp () throws Exception {
        String ds = MongoDaoTestITCase.AYRONA_TEST_DB_NAME+"_import_bulk_data";
        System.setProperty (ConfigurationConstants.PROP_MONGODB_DS, ds);
        mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (ds);
        dao = new MongoDao (mongoClient, ds);
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

        List<Bar> barList2 = Arrays.asList (
                new Bar (DateUtils.parseDate ("01.01.2017 01:35:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:40:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:45:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:50:00"), i++, i, i, i, i),
                new Bar (DateUtils.parseDate ("01.01.2017 01:55:00"), i++, i, i, i, i)
        );

        ImportMarketDataBatchJob importMarketDataBatchJob = new ImportMarketDataBatchJob ("TEST", barList, mongoClient, 2);

        BatchJobModel batchJobModel = new BatchJobModel ();
        batchJobModel.setType (importMarketDataBatchJob.getType ());
        batchJobModel.setStatus (BatchJob.Status.WAITING);
        batchJobModel.setProgress (0);
        batchJobModel.setStartDate (new Date ());
        dao.createBatchJob (batchJobModel);

        importMarketDataBatchJob.setId (batchJobModel.getId ());
        importMarketDataBatchJob.setCallback (new BatchJobManager.Callback(dao));
        importMarketDataBatchJob.run ();

        assertEquals (0, importMarketDataBatchJob.getFailedRowCount ());

        importMarketDataBatchJob = new ImportMarketDataBatchJob ("TEST", barList2, mongoClient, 1);

        batchJobModel = new BatchJobModel ();
        batchJobModel.setType (importMarketDataBatchJob.getType ());
        batchJobModel.setStatus (BatchJob.Status.WAITING);
        batchJobModel.setProgress (0);
        batchJobModel.setStartDate (new Date ());
        dao.createBatchJob (batchJobModel);

        importMarketDataBatchJob.setId (batchJobModel.getId ());
        importMarketDataBatchJob.setCallback (new BatchJobManager.Callback(dao));
        importMarketDataBatchJob.run ();

        assertEquals (3, importMarketDataBatchJob.getFailedRowCount ());

        List<MarketDataModel> actualMarketDataModelList = dao.findMarketData (Symbols.of ("TEST"), Period.M5, DateUtils.parseDate ("01.01.2017 02:00:00"), 12);

        assertEquals (12, actualMarketDataModelList.size ());
    }

}