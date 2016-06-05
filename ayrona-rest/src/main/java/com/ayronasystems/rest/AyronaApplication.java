package com.ayronasystems.rest;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.batchjob.BatchJobManager;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.mongo.MongoDao;
import com.ayronasystems.rest.resources.AccountResourceImpl;
import com.ayronasystems.rest.resources.AuthResourceImpl;
import com.ayronasystems.rest.resources.MarketDataResourceImpl;
import com.ayronasystems.rest.resources.StrategyResourceImpl;
import com.ayronasystems.rest.security.TokenManager;
import com.mongodb.MongoClient;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaApplication extends Application {

    private Set<Object> resources = new HashSet<Object> ();

    public AyronaApplication () {
        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        Dao dao = new MongoDao (mongoClient);
        BatchJobManager batchJobManager = Singletons.INSTANCE.getBatchJobManager ();
        resources.add (new StrategyResourceImpl ());
        resources.add (new AuthResourceImpl (dao, TokenManager.getInstance ()));
        resources.add (new AccountResourceImpl ());
        resources.add (new MarketDataResourceImpl (mongoClient, batchJobManager));
    }

    @Override
    public Set<Object> getSingletons () {
        return resources;
    }

}
