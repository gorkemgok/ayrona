package com.ayronasystems.core;

import com.ayronasystems.core.batchjob.BatchJobManager;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.core.service.discovery.ConsulServiceExplorer;
import com.ayronasystems.core.service.discovery.ServiceExplorer;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class Singletons {

    private static Logger log = LoggerFactory.getLogger (Singletons.class);

    private static Configuration conf = Configuration.getInstance ();

    public static final Singletons INSTANCE = new Singletons ();

    private transient MongoClient mongoClient = null;

    private transient ConsulServiceExplorer serviceExplorer = null;

    private transient BatchJobManager batchJobManager = null;

    private final Object lock1 = new Object ();

    private final Object lock2 = new Object ();

    private final Object lock3 = new Object ();

    private Singletons () {
    }

    public MongoClient getMongoClient () {
        if ( mongoClient == null){
            synchronized (lock1){
                if ( mongoClient == null){
                    try {
                        mongoClient = new MongoClient (conf.getString (ConfKey.MONGODB_HOST), conf.getInteger (ConfKey.MONGODB_PORT));
                    } catch ( UnknownHostException e ) {
                        log.error ("Can connect to mongoDB", e);
                    }
                }
            }
        }
        return mongoClient;
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
}
