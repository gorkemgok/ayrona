package com.ayronasystems.data.listener;

import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class BarDBSaverListener implements BarListener {

    private static Logger log = LoggerFactory.getLogger (BarDBSaverListener.class);

    private MongoClient mongoClient;

    public BarDBSaverListener (MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void newBar (Symbol symbol, Period period, Bar bar) {
        DBCollection collection =
                mongoClient.getDB (MongoDao.AYRONA_MARKETDATA_DB_NAME)
                           .getCollection (symbol.getSymbolString ().toLowerCase ());
        DBObject dbObject = new BasicDBObject ("createDate", new Date ())
                .append ("symbol", symbol.toString ())
                .append ("period", period.toString ())
                .append ("periodDate", bar.getDate ())
                .append ("open", bar.getOpen ())
                .append ("high", bar.getHigh ())
                .append ("low", bar.getLow ())
                .append ("close", bar.getClose ())
                .append ("volume", bar.getVolume ());
        collection.insert (dbObject);
        log.info ("Bar saved to {} - {}, {}", symbol, period, bar);
    }
}
