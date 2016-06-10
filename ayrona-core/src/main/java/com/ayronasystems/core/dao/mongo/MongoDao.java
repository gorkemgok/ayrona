package com.ayronasystems.core.dao.mongo;

import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.*;
import com.ayronasystems.core.edr.Edr;
import com.ayronasystems.core.edr.EdrModule;
import com.ayronasystems.core.edr.EdrType;
import com.ayronasystems.core.util.MongoUtils;
import com.google.common.base.Optional;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class MongoDao implements Dao{

    public static final String AYRONA_DB_NAME = "ayrona";

    public static final String AYRONA_MARKETDATA_DB_NAME = "ayrona_marketdata";

    private final MongoClient mongoClient;

    private final Morphia morphia;

    private final Datastore appDatastore;

    private final Datastore appMarketDatastore;

    private String ayronaDbName;

    private String ayronaMarketdataDbName;

    public MongoDao (MongoClient mongoClient) {
        this(mongoClient, AYRONA_DB_NAME, AYRONA_MARKETDATA_DB_NAME);
    }

    public MongoDao (MongoClient mongoClient, String ayronaDbName, String ayronaMarketdataDbName) {
        this.ayronaDbName = ayronaDbName;
        this.ayronaMarketdataDbName = ayronaMarketdataDbName;

        this.mongoClient = mongoClient;
        this.morphia = new Morphia ();
        morphia.mapPackage ("com.ayronasystems.core.dao.model");
        appDatastore = morphia.createDatastore (mongoClient, ayronaDbName);
        appMarketDatastore = morphia.createDatastore (mongoClient, ayronaMarketdataDbName);
        appDatastore.ensureIndexes ();
        appMarketDatastore.ensureIndexes ();
    }

    public Optional<UserModel> findUserByLogin (String login) {
        UserModel userModel = appDatastore.createQuery (UserModel.class).filter ("login", login).get ();
        if (userModel != null){
            return Optional.of (userModel);
        }else{
            return Optional.absent ();
        }
    }

    public StrategyModel createStrategy (StrategyModel strategyModel) {
        appDatastore.save (strategyModel);
        return strategyModel;
    }

    public void updateStrategy (StrategyModel strategyModel) {
        Query<StrategyModel> query = appDatastore.createQuery (StrategyModel.class).filter ("_id", strategyModel.getObjectId ());
        UpdateOperations<StrategyModel> operations = appDatastore.createUpdateOperations (StrategyModel.class)
                .set ("code", strategyModel.getCode ())
                .set ("name", strategyModel.getName ());
        appDatastore.update (query, operations);
    }

    public List<StrategyModel> findAllStrategies () {
        return appDatastore.createQuery (StrategyModel.class).asList ();
    }

    public Optional<StrategyModel> findStrategy (String id) {
        StrategyModel strategyModel = appDatastore.get (StrategyModel.class, new ObjectId (id));
        if (strategyModel == null){
            return Optional.absent ();
        }else {
            return Optional.of (strategyModel);
        }
    }

    public void bindAccountToStrategy (String strategyId, String accountId) {
        UpdateOperations<StrategyModel> operations = appDatastore.createUpdateOperations (StrategyModel.class)
                                                                 .add ("boundAccounts",
                                                                       new AccountBinder (accountId, AccountBinder.State.ACTIVE)
                                                                 );
        Query<StrategyModel> query = appDatastore.createQuery (StrategyModel.class).filter ("_id", new ObjectId (strategyId));
        appDatastore.update (query, operations);
    }

    public List<AccountModel> findBoundAccounts (String id) {
        StrategyModel strategyModel = appDatastore.get (StrategyModel.class, new ObjectId (id));
        List<ObjectId> objectIdList = MongoUtils.convertToObjectIdsAB (strategyModel.getBoundAccounts ());
        return appDatastore.get (AccountModel.class, objectIdList).asList ();
    }

    public List<AccountModel> findAllAccounts () {
        List<AccountModel> accountModelList = appDatastore.createQuery (AccountModel.class).asList ();
        return accountModelList;
    }

    public Optional<AccountModel> findAccount (String id) {
        AccountModel accountModel = appDatastore.get (AccountModel.class, new ObjectId (id));
        if (accountModel == null){
            return Optional.absent ();
        }else{
            return Optional.of (accountModel);
        }
    }

    public AccountModel createAccount (AccountModel accountModel) {
        appDatastore.save (accountModel);
        return accountModel;
    }

    public void deleteAccount (String id) {
        appDatastore.delete (AccountModel.class, new ObjectId (id));
    }

    public AccountModel updateAccount (AccountModel accountModel) {
        appDatastore.save (accountModel);
        return accountModel;
    }

    public Optional<BatchJobModel> findBatchJob (String id) {
        BatchJobModel batchJobModel = appDatastore.get (BatchJobModel.class, new ObjectId (id));
        if (batchJobModel != null){
            return Optional.of (batchJobModel);
        }
        return Optional.absent ();
    }

    public List<BatchJobModel> findAllBatchJobs () {
        return appDatastore.createQuery (BatchJobModel.class).asList ();
    }

    public BatchJobModel createBatchJob (BatchJobModel batchJobModel) {
        appDatastore.save (batchJobModel);
        return batchJobModel;
    }

    public void updateBatchJob (String id, BatchJob.Status status) {
        DBCollection collection = mongoClient.getDB (ayronaDbName).getCollection ("batch_job");
        DBObject query = new BasicDBObject ("_id", new ObjectId (id));
        DBObject update = new BasicDBObject ("$set", new BasicDBObject ("status", status.toString ()));
        collection.findAndModify (query, update);
    }

    public void updateBatchJob (String id, int progress) {
        DBCollection collection = mongoClient.getDB (ayronaDbName).getCollection ("batch_job");
        DBObject query = new BasicDBObject ("_id", new ObjectId (id));
        DBObject update = new BasicDBObject ("$set", new BasicDBObject ("progress", progress));
        collection.findAndModify (query, update);
    }

    public MarketDataAnalyzeModel createMarketDataAnalyze (MarketDataAnalyzeModel marketDataAnalyzeModel) {
        appDatastore.save (marketDataAnalyzeModel);
        return marketDataAnalyzeModel;
    }

    public Optional<MarketDataAnalyzeModel> findMarketDataAnalyze (String id) {
        MarketDataAnalyzeModel marketDataAnalyzeModel = appDatastore.get (MarketDataAnalyzeModel.class, new ObjectId (id));
        if (marketDataAnalyzeModel == null){
            return Optional.absent ();
        }
        return Optional.of (marketDataAnalyzeModel);
    }

    public EdrModel createEdr(EdrModel edrModel) {
        appDatastore.save(edrModel);
        return edrModel;
    }

    public List<EdrModel> findEdr(EdrModule edrModule, Date startDate, Date endDate) {
        Query<EdrModel> query = appDatastore.createQuery(EdrModel.class)
                .field("module").equal(edrModule)
                .field("createDate").greaterThanOrEq(startDate)
                .field("createDate").lessThanOrEq(endDate);
        return query.asList();
    }

    public List<EdrModel> findEdr(EdrType edrType, Date startDate, Date endDate) {
        Query<EdrModel> query = appDatastore.createQuery(EdrModel.class)
                .field("type").equal(edrType)
                .field("createDate").greaterThanOrEq(startDate)
                .field("createDate").lessThanOrEq(endDate);
        return query.asList();
    }

    public List<EdrModel> findEdrByAccountId(String accountId, Date startDate, Date endDate) {
        Query<EdrModel> query = appDatastore.createQuery(EdrModel.class)
                .field("properties.accountId").equal(accountId)
                .field("createDate").greaterThanOrEq(startDate)
                .field("createDate").lessThanOrEq(endDate);
        return query.asList();
    }

    public List<EdrModel> findEdrByStrategyId(String strategyId, Date startDate, Date endDate) {
        Query<EdrModel> query = appDatastore.createQuery(EdrModel.class)
                .field("properties.strategytId").equal(strategyId)
                .field("createDate").greaterThanOrEq(startDate)
                .field("createDate").lessThanOrEq(endDate);
        return query.asList();
    }
}
