package com.ayronasystems.core.dao.mongo;

import com.ayronasystems.core.BasicInitiator;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.*;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import com.google.common.base.Optional;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class MongoDaoTestITCase {

    public static final String AYRONA_TEST_DB_NAME = "test_ayrona";

    private Dao dao;

    @Before
    public void setUp () throws Exception {
        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (AYRONA_TEST_DB_NAME);

        dao = new MongoDao (mongoClient, AYRONA_TEST_DB_NAME);

    }

    @Test
    public void createMarketData(){
        Symbol symbol = Symbols.of ("TEST");
        Period period = Period.M15;
        List<MarketDataModel> expectedMarketDataModelList = new ArrayList<MarketDataModel> ();
        for ( int i = 0; i < 100; i++ ) {
            MarketDataModel marketDataModel = new MarketDataModel ();
            marketDataModel.setSymbol (symbol);
            marketDataModel.setPeriod (period);
            marketDataModel.setPeriodDate (new Date (i*period.getAsMillis ()));
            marketDataModel.setOpen (1*i);
            marketDataModel.setHigh (2*i);
            marketDataModel.setLow (3*i);
            marketDataModel.setClose (4*i);
            marketDataModel.setVolume (5*i);
            expectedMarketDataModelList.add (marketDataModel);
        }
        for(MarketDataModel actualMarketDataModel : expectedMarketDataModelList) {
            dao.createMarketData (actualMarketDataModel);
        }

        long start = System.currentTimeMillis ();
        List<MarketDataModel> actualMarketDataModelList = dao.findMarketData (symbol, period, new Date(0), new Date (100 * period.getAsMillis ()));
        long end = System.currentTimeMillis ();
        System.out.println (actualMarketDataModelList.size () + " in " + (end-start) + " ms");

        assertEquals (100, actualMarketDataModelList.size ());
        for ( int i = 0; i < 100; i++ ) {
            assertEquals (expectedMarketDataModelList.get (i).getSymbol (), actualMarketDataModelList.get (i).getSymbol ());
            assertEquals (expectedMarketDataModelList.get (i).getPeriod (), actualMarketDataModelList.get (i).getPeriod ());
            assertEquals (expectedMarketDataModelList.get (i).getPeriodDate (), actualMarketDataModelList.get (i).getPeriodDate ());
            assertEquals (expectedMarketDataModelList.get (i).getOpen (), actualMarketDataModelList.get (i).getOpen (), 0);
            assertEquals (expectedMarketDataModelList.get (i).getHigh (), actualMarketDataModelList.get (i).getHigh (), 0);
            assertEquals (expectedMarketDataModelList.get (i).getLow (), actualMarketDataModelList.get (i).getLow (), 0);
            assertEquals (expectedMarketDataModelList.get (i).getClose (), actualMarketDataModelList.get (i).getClose (), 0);
            assertEquals (expectedMarketDataModelList.get (i).getVolume (), actualMarketDataModelList.get (i).getVolume (), 0);

            if (i>0){
                assertTrue (actualMarketDataModelList.get (i).getPeriodDate ().after (
                        actualMarketDataModelList.get (i-1).getPeriodDate ()
                ));
            }
        }

        actualMarketDataModelList = dao.findMarketData (symbol, period, new Date(99*period.getAsMillis ()),10);
        assertEquals (10, actualMarketDataModelList.size ());
        assertEquals (new Date(89*period.getAsMillis ()), actualMarketDataModelList.get (0).getPeriodDate ());
        assertEquals (new Date(98*period.getAsMillis ()), actualMarketDataModelList.get (actualMarketDataModelList.size ()-1).getPeriodDate ());

        actualMarketDataModelList = dao.findMarketData (symbol, period, new Date(89*period.getAsMillis ()),90);
        assertEquals (89, actualMarketDataModelList.size ());
        assertEquals (new Date(0), actualMarketDataModelList.get (0).getPeriodDate ());
        assertEquals (new Date(88*period.getAsMillis ()), actualMarketDataModelList.get (actualMarketDataModelList.size ()-1).getPeriodDate ());

    }

    @Test
    public void createAndFindUser(){
        UserModel expectedUserModel = new UserModel ();
        expectedUserModel.setName ("Görkem the user");
        expectedUserModel.setLogin ("gorko");
        expectedUserModel.setPassword ("123456");
        dao.createUser (expectedUserModel);

        Optional<UserModel> actualUserModelOptional = dao.findUserByLogin ("gorko");

        assertTrue (actualUserModelOptional.isPresent ());
        UserModel actualUserModel = actualUserModelOptional.get ();
        assertEquals (expectedUserModel.getId (), actualUserModel.getId ());
    }

    @Test
    public void createUpdateFindOneAndDeleteAccount () throws Exception {
        AccountModel expectedAccountModel = new AccountModel ();
        expectedAccountModel.setAccountantName ("Kemalettin");
        expectedAccountModel.setType (AccountModel.Type.MT4);
        LoginDetail loginDetail = new LoginDetail ();
        loginDetail.setServer ("Gedik");
        loginDetail.setId ("loginullah");
        loginDetail.setPassword ("ziyaaaaa");
        expectedAccountModel.setLoginDetail (loginDetail);
        String id = dao.createAccount (expectedAccountModel).getId ();

        expectedAccountModel.setType (AccountModel.Type.ATA_CUSTOM);
        dao.updateAccount (expectedAccountModel);

        Optional<AccountModel> accountModelOptional = dao.findAccount (id);
        assertTrue (accountModelOptional.isPresent ());

        AccountModel actualAccountModel = accountModelOptional.get ();

        assertEquals (id, actualAccountModel.getId ());
        assertEquals (expectedAccountModel.getAccountantName (), actualAccountModel.getAccountantName ());
        assertEquals (expectedAccountModel.getType (), actualAccountModel.getType ());
        assertEquals (expectedAccountModel.getLoginDetail ().getServer (),
                      actualAccountModel.getLoginDetail ().getServer ());
        assertEquals (expectedAccountModel.getLoginDetail ().getId (),
                      actualAccountModel.getLoginDetail ().getId ());
        assertEquals (expectedAccountModel.getLoginDetail ().getPassword (),
                      actualAccountModel.getLoginDetail ().getPassword ());

        dao.deleteAccount (id);

        accountModelOptional = dao.findAccount (id);
        assertFalse (accountModelOptional.isPresent ());
    }

    @Test
    public void createAndFindAllAccounts () throws Exception {
        AccountModel expectedAccountModel = new AccountModel ();
        expectedAccountModel.setAccountantName ("Görkem Gök");
        expectedAccountModel.setType (AccountModel.Type.MT4);
        LoginDetail loginDetail = new LoginDetail ();
        loginDetail.setServer ("AtaOnline-Demo");
        loginDetail.setId ("1218368283");
        loginDetail.setPassword ("gm7xtnn");
        expectedAccountModel.setLoginDetail (loginDetail);
        String id = dao.createAccount (expectedAccountModel).getId ();

        List<AccountModel> accountModelList = dao.findAllAccounts ();
        assertEquals (1, accountModelList.size ());

        AccountModel actualAccountModel = accountModelList.get (0);

        assertEquals (id, actualAccountModel.getId ());
        assertEquals (expectedAccountModel.getAccountantName (), actualAccountModel.getAccountantName ());
        assertEquals (expectedAccountModel.getType (), actualAccountModel.getType ());
        assertEquals (expectedAccountModel.getLoginDetail ().getServer (),
                      actualAccountModel.getLoginDetail ().getServer ());
        assertEquals (expectedAccountModel.getLoginDetail ().getId (),
                      actualAccountModel.getLoginDetail ().getId ());
        assertEquals (expectedAccountModel.getLoginDetail ().getPassword (),
                      actualAccountModel.getLoginDetail ().getPassword ());
    }

    @Test
    public void createAndUpdateBatchJob() throws Exception{
        Optional<BatchJobModel> batchJobModelOptional = dao.findBatchJob ("57543b51a826c4e7e83d57fd");

        assertFalse (batchJobModelOptional.isPresent ());

        Date date = new Date ();
        BatchJobModel expectedBatchJobModel = new BatchJobModel ();
        expectedBatchJobModel.setStatus (BatchJob.Status.WAITING);
        expectedBatchJobModel.setType (BatchJob.Type.IMPORT_MARKETDATA);
        expectedBatchJobModel.setProgress (0);
        expectedBatchJobModel.setStartDate (date);

        String id = dao.createBatchJob (expectedBatchJobModel).getId ();

        batchJobModelOptional = dao.findBatchJob (id);

        assertTrue (batchJobModelOptional.isPresent ());

        BatchJobModel actualBatchJobModel = dao.findBatchJob (id).get ();

        assertEquals (expectedBatchJobModel.getId (), actualBatchJobModel.getId ());
        assertEquals (expectedBatchJobModel.getStartDate (), actualBatchJobModel.getStartDate ());
        assertEquals (expectedBatchJobModel.getStatus (), actualBatchJobModel.getStatus ());
        assertEquals (expectedBatchJobModel.getType (), actualBatchJobModel.getType ());
        assertEquals (expectedBatchJobModel.getProgress (), actualBatchJobModel.getProgress ());

        dao.updateBatchJob (id, 30);
        dao.updateBatchJob (id, BatchJob.Status.COMPLETED);

        batchJobModelOptional = dao.findBatchJob (id);
        assertTrue (batchJobModelOptional.isPresent ());
        actualBatchJobModel = batchJobModelOptional.get ();
        assertEquals (30, actualBatchJobModel.getProgress ());
        assertEquals (BatchJob.Status.COMPLETED, actualBatchJobModel.getStatus ());

        BatchJobModel expectedBatchJob2 = new BatchJobModel ();
        expectedBatchJob2.setStartDate (new Date());
        expectedBatchJob2.setType (BatchJob.Type.ANALYZE_MARKETDATA);
        expectedBatchJob2.setStatus (BatchJob.Status.COMPLETED);
        expectedBatchJob2.setProgress (100);

        dao.createBatchJob (expectedBatchJob2);

        List<BatchJobModel> batchJobModelList = dao.findAllBatchJobs ();

        assertEquals (2, batchJobModelList.size ());

    }

    @Test
    public void createAndFindAllStrategiesAndFindBoundAccounts(){
        StrategyModel expectedStrategyModel = new StrategyModel ();
        expectedStrategyModel.setName ("Fatih ate");
        expectedStrategyModel.setCode ("<code></code>");

        AccountModel expectedAccountModel = new AccountModel ();
        expectedAccountModel.setAccountantName ("Fatih");
        expectedAccountModel.setType (AccountModel.Type.ATA_CUSTOM);
        LoginDetail loginDetail = new LoginDetail ();
        loginDetail.setServer ("");
        loginDetail.setId ("4534523452");
        loginDetail.setPassword ("");
        expectedAccountModel.setLoginDetail (loginDetail);
        dao.createAccount (expectedAccountModel);

        expectedStrategyModel.setAccounts (Arrays.asList (new AccountBinder (expectedAccountModel.getId (),
                                                                             AccountBinder.State.ACTIVE, 1)));
        dao.createStrategy (expectedStrategyModel);

        List<StrategyModel> actualStrategyModelList = dao.findAllStrategies ();

        assertEquals (1, actualStrategyModelList.size ());

        StrategyModel actualStrategyModel = actualStrategyModelList.get (0);

        assertEquals (expectedStrategyModel.getId (), actualStrategyModel.getId ());
        assertEquals (expectedStrategyModel.getName (), actualStrategyModel.getName ());
        assertEquals (expectedStrategyModel.getCode (), actualStrategyModel.getCode ());

        assertEquals (1, actualStrategyModel.getAccounts ().size ());
        assertEquals (expectedAccountModel.getId (), actualStrategyModel.getAccounts ().get (0).getId ());

        List<AccountModel> accountModelList = dao.findBoundAccounts (actualStrategyModel.getId ());
        assertEquals (1, accountModelList.size ());

        AccountModel actualAccountModel = accountModelList.get (0);
        assertEquals (expectedAccountModel.getAccountantName (), actualAccountModel.getAccountantName ());

    }

    @Test
    public void bindFindUpdateUnbindBoundAccount(){
        StrategyModel expectedStrategyModel = new StrategyModel ();
        expectedStrategyModel.setName ("Fatih ate 2");
        expectedStrategyModel.setCode ("<code2></code2>");
        dao.createStrategy (expectedStrategyModel);

        AccountModel expectedAccountModel = new AccountModel ();
        expectedAccountModel.setAccountantName ("Fatih2");
        expectedAccountModel.setType (AccountModel.Type.ATA_CUSTOM);
        LoginDetail loginDetail = new LoginDetail ();
        loginDetail.setServer ("");
        loginDetail.setId ("23423423424");
        loginDetail.setPassword ("");
        expectedAccountModel.setLoginDetail (loginDetail);
        dao.createAccount (expectedAccountModel);

        List<StrategyModel> actualStrategyModelList = dao.findBoundStrategies (expectedAccountModel.getId ());
        assertEquals (0, actualStrategyModelList.size ());

        AccountBinder accountBinder = new AccountBinder ();
        accountBinder.setId (expectedAccountModel.getId ());
        accountBinder.setLot (1);
        accountBinder.setState (AccountBinder.State.ACTIVE);
        dao.bindAccountToStrategy (expectedStrategyModel.getId (), accountBinder);

        actualStrategyModelList = dao.findBoundStrategies (expectedAccountModel.getId ());
        assertEquals (1, actualStrategyModelList.size ());
        StrategyModel actualStrategyModel = actualStrategyModelList.get (0);
        assertEquals (expectedStrategyModel.getId (), actualStrategyModel.getId ());
        assertEquals (expectedStrategyModel.getName (), actualStrategyModel.getName ());
        assertEquals (expectedStrategyModel.getCode (), actualStrategyModel.getCode ());

        List<AccountModel> actualAccountList = dao.findBoundAccounts (expectedStrategyModel.getId ());
        assertEquals (1, actualAccountList.size ());

        AccountModel actualAccountModel = actualAccountList.get (0);
        assertEquals (expectedAccountModel.getAccountantName (), actualAccountModel.getAccountantName ());

        Optional<StrategyModel> strategyModelOptional = dao.findStrategy (expectedStrategyModel.getId ());
        assertTrue (strategyModelOptional.isPresent ());
        actualStrategyModel = strategyModelOptional.get ();
        assertEquals (1, actualStrategyModel.getAccounts ().size ());
        AccountBinder actualAccountBinder = actualStrategyModel.getAccounts ().get (0);
        assertEquals (AccountBinder.State.ACTIVE, actualAccountBinder.getState ());
        assertEquals (accountBinder.getId (), actualAccountBinder.getId ());
        assertEquals (accountBinder.getLot (), actualAccountBinder.getLot (), 0);

        accountBinder.setState (AccountBinder.State.INACTIVE);
        accountBinder.setLot (2);
        dao.updateBoundAccount (expectedStrategyModel.getId (), accountBinder);
        strategyModelOptional = dao.findStrategy (expectedStrategyModel.getId ());
        assertTrue (strategyModelOptional.isPresent ());
        actualStrategyModel = strategyModelOptional.get ();
        assertEquals (1, actualStrategyModel.getAccounts ().size ());
        actualAccountBinder = actualStrategyModel.getAccounts ().get (0);
        assertEquals (AccountBinder.State.INACTIVE, actualAccountBinder.getState ());
        assertEquals (accountBinder.getId (), actualAccountBinder.getId ());
        assertEquals (accountBinder.getLot (), actualAccountBinder.getLot (), 0);

    }

    @Test
    public void createAndClosePosition(){
        Date openDate = new Date(1000);
        Date idealOpenDate = new Date(1010);
        Date closeDate = new Date(2000);
        Date idealCloseDate = new Date(2010);
        Position expectedPosition = Position
                .builder (new BasicInitiator ("strategyId1", "strategyName1"))
                .symbol (Symbols.of ("TEST"))
                .lot (1)
                .direction (Direction.LONG)
                .openDate (idealOpenDate)
                .openPrice (10)
                .takeProfit (14)
                .stopLoss (8)
                .description ("Test test")
                .build ();
        expectedPosition.setOpenDate (openDate);
        expectedPosition.setOpenPrice (12);
        PositionModel expectedPositionModel = PositionModel.valueOf (expectedPosition, new BasicAccount ("accountId1"));

        dao.createPosition (expectedPositionModel);

        List<PositionModel> positionList = dao.findAllPositions ();

        assertEquals (1, positionList.size ());

        PositionModel actualPositionModel = positionList.get (0);

        Position actualPosition = actualPositionModel.toPosition ();

        assertTrue (expectedPosition.isSame (actualPosition));

        actualPosition.close (idealCloseDate, 14);
        actualPosition.setClosePrice (15);
        actualPosition.setCloseDate (idealCloseDate);

        expectedPositionModel = PositionModel.valueOf (actualPosition, new BasicAccount ("accountId1"));

        dao.closePosition (expectedPositionModel);

        positionList = dao.findAllPositions ();

        assertEquals (1, positionList.size ());

        actualPositionModel = positionList.get (0);

        assertTrue (actualPosition.isClosed ());

        actualPosition = actualPositionModel.toPosition ();

        expectedPosition = expectedPositionModel.toPosition ();

        assertTrue (expectedPosition.isSame (actualPosition));
    }

}