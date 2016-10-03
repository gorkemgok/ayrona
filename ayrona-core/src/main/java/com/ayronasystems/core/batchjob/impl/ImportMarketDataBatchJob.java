package com.ayronasystems.core.batchjob.impl;

import com.ayronasystems.core.batchjob.AbstractBatchJob;
import com.ayronasystems.core.batchjob.BatchJobResult;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 05/06/16.
 */
public class ImportMarketDataBatchJob extends AbstractBatchJob {

    private static Logger log = LoggerFactory.getLogger (ImportMarketDataBatchJob.class);

    private static Configuration conf = Configuration.getInstance ();

    private List<Bar> barList;

    private MongoClient mongoClient;

    private String symbol;

    private int bulkSize;

    private int failedRowCount;

    public ImportMarketDataBatchJob (String symbol, List<Bar> barList, MongoClient mongoClient) {
        this(symbol, barList, mongoClient, 1000);
    }

    public ImportMarketDataBatchJob (String symbol, List<Bar> barList, MongoClient mongoClient, int bulkSize) {
        this.symbol = symbol.toLowerCase ();
        this.barList = barList;
        this.mongoClient = mongoClient;
        this.bulkSize = bulkSize;
    }

    public Type getType () {
        return Type.IMPORT_MARKETDATA;
    }

    public void run () {
        DBCollection collection = mongoClient.getDB (conf.getString(ConfKey.MONGODB_DS)).getCollection ("marketdata");
        BulkWriteOperation bwo = collection.initializeUnorderedBulkOperation ();
        long periodMillis = barList.get (1).getDate ().getTime () - barList.get (0).getDate ().getTime ();
        Period period = Period.parse (periodMillis);
        int count = barList.size ();
        int iu = 0;
        int progress = 0;
        int updateLimit = count / 100;
        int i = 0;
        long start = System.currentTimeMillis ();
        for ( Bar bar : barList ){
            DBObject dbObject = new BasicDBObject ("createDate", new Date ())
                    .append ("symbol", symbol.toUpperCase ())
                    .append ("period", period.toString ())
                    .append ("periodDate", bar.getDate ())
                    .append ("open", bar.getOpen ())
                    .append ("high", bar.getHigh ())
                    .append ("low", bar.getLow ())
                    .append ("close", bar.getClose ())
                    .append ("volume", bar.getVolume ());
            bwo.insert (dbObject);
            i++;
            if (i == bulkSize){
                i = 0;
                try {
                    BulkWriteResult bwr = bwo.execute ();
                }catch ( BulkWriteException ex){
                    failedRowCount += ex.getWriteErrors ().size ();
                }
                bwo = collection.initializeUnorderedBulkOperation ();
            }
            iu++;
            if (iu == updateLimit){
                progress++;
                batchJobCallback.update (this, progress);
                iu = 0;
            }
        }
        try {
            BulkWriteResult bwr = bwo.execute ();
        }catch ( BulkWriteException ex){
            failedRowCount += ex.getWriteErrors ().size ();
        }
        long end = System.currentTimeMillis ();
        BatchJobResult<Long> batchJobResult = new BatchJobResult (this, (end - start));
        batchJobCallback.complete (batchJobResult);
    }

    public int getFailedRowCount () {
        return failedRowCount;
    }
}
