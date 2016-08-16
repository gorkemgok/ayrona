package com.ayronasystems.core;

import com.ayronasystems.core.batchjob.BatchJobManager;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.core.service.BackTestService;
import com.ayronasystems.core.service.StandaloneBackTestService;
import com.ayronasystems.core.service.discovery.ConsulServiceExplorer;
import com.ayronasystems.core.service.discovery.ServiceExplorer;
import com.ayronasystems.core.util.calendar.MarketCalendarService;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class Singletons {

    private static Logger log = LoggerFactory.getLogger (Singletons.class);

    private static Configuration conf = Configuration.getInstance ();

    public static final Singletons INSTANCE = new Singletons ();

    private volatile MongoClient mongoClient = null;

    private volatile ConsulServiceExplorer serviceExplorer = null;

    private volatile BatchJobManager batchJobManager = null;

    private volatile BackTestService backTestService = null;

    private volatile MarketCalendarService marketCalendarService = null;

    private volatile Dao dao = null;

    private final Object lock1 = new Object ();

    private final Object lock2 = new Object ();

    private final Object lock3 = new Object ();

    private final Object lock4 = new Object ();

    private final Object lock5 = new Object ();

    private final Object lock6 = new Object ();

    private Singletons () {
    }

    public MongoClient getMongoClient () {
        if ( mongoClient == null){
            synchronized (lock1){
                if ( mongoClient == null){
                    try {
                        String host = conf.getString (ConfKey.MONGODB_HOST);
                        int port = conf.getInteger (ConfKey.MONGODB_PORT);
                        mongoClient = new MongoClient (host, port);
                        log.info ("Connected to mongodb {}:{}", host, port);
                    } catch ( Exception e ) {
                        log.error ("Can connect to mongoDB", e);
                    }
                }
            }
        }
        return mongoClient;
    }

    public Dao getDao () {
        if ( dao == null){
            synchronized (lock4){
                if ( dao == null){
                    dao = new MongoDao (getMongoClient (), conf.getString (ConfKey.MONGODB_DS));
                }
            }
        }
        return dao;
    }

    public ServiceExplorer getServiceExplorer (){
        if (serviceExplorer == null){
            synchronized (lock2){
                if (serviceExplorer == null){
                    serviceExplorer = new ConsulServiceExplorer (conf.getString (ConfKey.CONSUL_HOST), conf.getInteger (ConfKey.CONSUL_PORT));
                }
            }
        }
        return serviceExplorer;
    }

    public BatchJobManager getBatchJobManager (){
        if (batchJobManager == null){
            synchronized (lock3){
                if (batchJobManager == null){
                    batchJobManager = new BatchJobManager (new MongoDao (getMongoClient ()), 10);
                }
            }
        }
        return batchJobManager;
    }

    public BackTestService getBackTestService () {
        if ( backTestService == null){
            synchronized (lock5){
                if ( backTestService == null){
                    backTestService = new StandaloneBackTestService ();
                }
            }
        }
        return backTestService;
    }

    public MarketCalendarService getMarketCalendarService () {
        if ( marketCalendarService == null){
            synchronized (lock6){
                if ( marketCalendarService == null){
                    marketCalendarService = new MarketCalendarService ();
                }
            }
        }
        return marketCalendarService;
    }
}
