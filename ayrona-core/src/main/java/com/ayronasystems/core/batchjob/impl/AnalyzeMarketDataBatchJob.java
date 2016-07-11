package com.ayronasystems.core.batchjob.impl;

import com.ayronasystems.core.batchjob.AbstractBatchJob;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AbsentBar;
import com.ayronasystems.core.dao.model.MarketDataAnalyzeModel;
import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 05/06/16.
 */
public class AnalyzeMarketDataBatchJob extends AbstractBatchJob{

    private Symbol symbol;

    private Period period;

    private MongoClient mongoClient;

    public AnalyzeMarketDataBatchJob (Symbol symbol, Period period, MongoClient mongoClient) {
        this.symbol = symbol;
        this.period = period;
        this.mongoClient = mongoClient;
    }

    public Type getType () {
        return Type.ANALYZE_MARKETDATA;
    }

    public void run () {
        Dao dao = new MongoDao (mongoClient);
        DBCollection collection = mongoClient.getDB (MongoDao.AYRONA_MARKETDATA_DB_NAME).getCollection (symbol.getName ().toLowerCase ());
        DBCursor cursor = collection.find (new BasicDBObject ("symbol", symbol.getName ()).append ("period", period.toString ()))
                                    .sort (new BasicDBObject ("periodDate", 1));
        MarketDataAnalyzeModel marketDataAnalyzeModel = new MarketDataAnalyzeModel ();
        List<AbsentBar> absentBarList = new ArrayList<AbsentBar> ();
        marketDataAnalyzeModel.setAbsentBarList (absentBarList);
        Date lastDate = null;
        while ( cursor.hasNext () ){
            DBObject dbObject = cursor.next ();
            Date date = (Date)dbObject.get ("periodDate");
            if (lastDate != null){
                long time = date.getTime ();
                long lastTime = lastDate.getTime ();
                long diff = time - lastTime;
                if (diff != period.getAsMillis ()){
                    AbsentBar absentBar = new AbsentBar ();
                    absentBar.setStartDate (lastDate);
                    absentBar.setEndDate (date);
                    absentBar.setAbsentBarCount ((int)(diff / period.getAsMillis ()));
                    absentBarList.add (absentBar);
                }
            }
            lastDate = date;
        }
        dao.createMarketDataAnalyze (marketDataAnalyzeModel);
    }
}
