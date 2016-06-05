package com.ayronasystems.core.batchjob.impl;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.MarketDataAnalyzeModel;
import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by gorkemgok on 06/06/16.
 */
public class AnalyzeMarketDataBatchJobTest {

    private Dao dao;
    @Before
    public void setUp () throws Exception {
        dao = new MongoDao (Singletons.INSTANCE.getMongoClient ());

    }

    @Test
    public void run () throws Exception {
        AnalyzeMarketDataBatchJob batchJob = new AnalyzeMarketDataBatchJob (
                Symbol.VOB30, Period.M5, Singletons.INSTANCE.getMongoClient ()
        );

        batchJob.run ();
        Optional<MarketDataAnalyzeModel> modelOptional = dao.findMarketDataAnalyze ("57549572a8269a7af9b096e8");

        if (modelOptional.isPresent ()){
            MarketDataAnalyzeModel marketDataAnalyzeModel = modelOptional.get ();
            Date date = marketDataAnalyzeModel.getAbsentBarList ().get (0).getStartDate ();
            Date date2 = marketDataAnalyzeModel.getAbsentBarList ().get (2190).getStartDate ();
            int i = 0;
        }
    }

}