package com.ayronasystems.core.dao.mongo;

import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.*;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.edr.EdrModule;
import com.ayronasystems.core.edr.EdrType;
import com.ayronasystems.core.util.MongoUtils;
import com.google.common.base.Optional;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class MongoDao implements Dao{

    private static final String MAP_PACKAGE = "com.ayronasystems.core.dao.model";

    public static final String AYRONA_DB_NAME = "ayrona";

    public static final String AYRONA_MARKETDATA_DB_NAME = "ayrona_marketdata";

    private final MongoClient mongoClient;

    private final Morphia morphia;

    private final Datastore appDatastore;

    private String ayronaDbName;

    public MongoDao (MongoClient mongoClient) {
        this(mongoClient, AYRONA_DB_NAME);
    }

    public MongoDao (MongoClient mongoClient, String ayronaDbName) {
        this.ayronaDbName = ayronaDbName;

        this.mongoClient = mongoClient;
        this.morphia = new Morphia ();
        morphia.mapPackage (MAP_PACKAGE);
        appDatastore = morphia.createDatastore (mongoClient, ayronaDbName);
        appDatastore.ensureIndexes ();
    }

    public Optional<UserModel> findUserByLogin (String login) {
        UserModel userModel = appDatastore.createQuery (UserModel.class).filter ("login", login).get ();
        if (userModel != null){
            return Optional.of (userModel);
        }else{
            return Optional.absent ();
        }
    }

    public UserModel createUser (UserModel userModel) {
        appDatastore.save (userModel);
        return userModel;
    }

    public StrategyStat getStrategyStat () {
        long activeCount = appDatastore.getCount (appDatastore.createQuery (StrategyModel.class).field ("state").equal (AccountBinder.State.ACTIVE));
        long inactiveCount = appDatastore.getCount (appDatastore.createQuery (StrategyModel.class).field ("state").equal (AccountBinder.State.INACTIVE));
        return new StrategyStat (activeCount, inactiveCount);
    }

    public StrategyModel createStrategy (StrategyModel strategyModel) {
        appDatastore.save (strategyModel);
        return strategyModel;
    }

    public boolean updateStrategy (StrategyModel strategyModel) {
        Query<StrategyModel> query = appDatastore.createQuery (StrategyModel.class).filter (Mapper.ID_KEY, strategyModel.getObjectId ());
        return appDatastore.updateFirst (query, strategyModel, false).getUpdatedCount () > 0;
    }

    public boolean deleteStrategy (String strategyId) {
        WriteResult result = appDatastore.delete (StrategyModel.class, new ObjectId (strategyId));
        return result.getN () > 0;
    }

    public List<StrategyModel> findAllStrategies () {
        return appDatastore.createQuery (StrategyModel.class).asList ();
    }

    public List<StrategyModel> findActiveStrategies () {
        return appDatastore.createQuery (StrategyModel.class).field ("state").equal (AccountBinder.State.ACTIVE).asList ();
    }

    public Optional<StrategyModel> findStrategy (String id) {
        StrategyModel strategyModel = appDatastore.get (StrategyModel.class, new ObjectId (id));
        if (strategyModel == null){
            return Optional.absent ();
        }else {
            return Optional.of (strategyModel);
        }
    }

    public void bindAccountToStrategy (String strategyId, AccountBinder accountBinder) {
        UpdateOperations<StrategyModel> operations = appDatastore.createUpdateOperations (StrategyModel.class)
                                                                 .add ("accounts", accountBinder);
        Query<StrategyModel> query = appDatastore.createQuery (StrategyModel.class).filter (
                Mapper.ID_KEY, new ObjectId (strategyId));
        appDatastore.update (query, operations);
    }

    public void updateBoundAccount (String strategyId, AccountBinder accountBinder) {
        UpdateOperations<StrategyModel> operations = appDatastore.createUpdateOperations (StrategyModel.class)
                                                                 .set ("accounts.$", accountBinder);
        Query<StrategyModel> query = appDatastore.createQuery (StrategyModel.class)
                                                 .field (Mapper.ID_KEY).equal (new ObjectId (strategyId))
                                                 .field ("accounts.id").equal (accountBinder.getId ());
        appDatastore.update (query, operations);
    }

    public void unboundAccount (String strategyId, String accountId) {
        UpdateOperations<StrategyModel> operations = appDatastore.createUpdateOperations (StrategyModel.class)
                                                                 .removeFirst ("accounts");
        Query<StrategyModel> query = appDatastore.createQuery (StrategyModel.class)
                                                 .field (Mapper.ID_KEY).equal (new ObjectId (strategyId))
                                                 .field ("accounts.id").equal (accountId);
        appDatastore.update (query, operations);
    }

    public List<AccountModel> findBoundAccounts (String id) {
        StrategyModel strategyModel = appDatastore.get (StrategyModel.class, new ObjectId (id));
        List<ObjectId> objectIdList = MongoUtils.convertToObjectIdsAB (strategyModel.getAccounts ());
        return appDatastore.get (AccountModel.class, objectIdList).asList ();
    }

    public long getAccountCount () {
        return appDatastore.getCount (appDatastore.createQuery (AccountModel.class));
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

    public List<StrategyModel> findBoundStrategies (String accountId) {
        List<StrategyModel> strategyModelList = appDatastore.createQuery (StrategyModel.class).disableValidation ()
                                                            .field ("accounts.id")
                                                            .equal (accountId).asList ();
        return strategyModelList;
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

    public boolean updateBatchJob (String id, BatchJob.Status status) {
        DBCollection collection = mongoClient.getDB (ayronaDbName).getCollection ("batch_job");
        DBObject query = new BasicDBObject ("_id", new ObjectId (id));
        DBObject update = new BasicDBObject ("$set", new BasicDBObject ("status", status.toString ()));
        return collection.findAndModify (query, update) != null;
    }

    public boolean updateBatchJob (String id, int progress) {
        DBCollection collection = mongoClient.getDB (ayronaDbName).getCollection ("batch_job");
        DBObject query = new BasicDBObject ("_id", new ObjectId (id));
        DBObject update = new BasicDBObject ("$set", new BasicDBObject ("progress", progress));
        return collection.findAndModify (query, update) != null;
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

    public PositionModel createPosition (PositionModel positionModel) {
        appDatastore.save (positionModel);
        return positionModel;
    }

    public boolean closePosition (PositionModel positionModel) {
        Query<PositionModel> query = appDatastore
                .createQuery (PositionModel.class)
                .field ("_id")
                .equal (positionModel.getObjectId ());
        UpdateOperations<PositionModel> update = appDatastore.createUpdateOperations (PositionModel.class)
                .set ("closeDate", positionModel.getCloseDate ())
                .set ("closePrice", positionModel.getClosePrice ())
                .set ("idealCloseDate", positionModel.getIdealCloseDate ())
                .set ("idealClosePrice", positionModel.getIdealClosePrice ())
                .set ("isClosed", true);
        return appDatastore.update (query, update).getUpdatedCount () > 0;
    }

    public List<PositionModel> findPositionsByAccountId (String accountId) {
        Query<PositionModel> query = appDatastore
                .createQuery (PositionModel.class)
                .field ("accountId")
                .equal (accountId);
        return query.asList ();
    }

    public List<PositionModel> findOpenPositionsByAccountId (String accountId) {
        Query<PositionModel> query = appDatastore
                .createQuery (PositionModel.class)
                .field ("accountId")
                .equal (accountId)
                .field ("isClosed")
                .equal (false);
        return query.asList ();
    }

    public List<PositionModel> findPositionsByStrategyId (String strategyId) {
        Query<PositionModel> query = appDatastore
                .createQuery (PositionModel.class)
                .field ("strategyId")
                .equal (strategyId);
        return query.asList ();
    }

    public List<PositionModel> findOpenPositionsByStrategyId (String strategyId) {
        Query<PositionModel> query = appDatastore
                .createQuery (PositionModel.class)
                .field ("strategyId")
                .equal (strategyId)
                .field ("isClosed")
                .equal (false);
        return query.asList ();
    }

    public List<PositionModel> findAllPositions () {
        return appDatastore.createQuery (PositionModel.class).asList ();
    }

    public MarketDataModel createMarketData (MarketDataModel marketDataModel) {
        appDatastore.save (marketDataModel);
        return marketDataModel;
    }

    public List<MarketDataModel> findMarketData (Symbol symbol, Period period, Date beginningDate, Date endDate) {
        List<MarketDataModel> marketDataModelList = appDatastore.createQuery (MarketDataModel.class)
                    .field ("symbol").equal (symbol.getName ())
                    .field ("period").equal (period)
                    .field ("periodDate").greaterThanOrEq (beginningDate)
                    .field ("periodDate").lessThan (endDate)
                    .order ("periodDate").asList ();
        return marketDataModelList;
    }

    public List<MarketDataModel> findMarketData (Symbol symbol, Period period, Date endDate, int count) {
        List<MarketDataModel> marketDataModelList = appDatastore.createQuery (MarketDataModel.class)
                                                                .field ("symbol").equal (symbol.getName ())
                                                                .field ("period").equal (period)
                                                                .field ("periodDate").lessThan (endDate)
                                                                .order ("-periodDate")
                                                                .limit (count).asList ();
        List<MarketDataModel> invertedMarketDataModelList = new ArrayList<MarketDataModel> (marketDataModelList.size ());
        for ( int i = marketDataModelList.size () - 1; i >= 0; i-- ) {
            invertedMarketDataModelList.add (marketDataModelList.get (i));
        }
        return invertedMarketDataModelList;
    }

}
