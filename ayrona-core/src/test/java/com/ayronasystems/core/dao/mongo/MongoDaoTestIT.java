package com.ayronasystems.core.dao.mongo;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.*;
import com.google.common.base.Optional;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class MongoDaoTestIT {

    public static final String AYRONA_TEST_DB_NAME = "ayrona_test";

    public static final String AYRONA_MARKETDATA_TEST_DB_NAME = "ayrona_marketdata_test";

    private Dao dao;

    @Before
    public void setUp () throws Exception {
        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (AYRONA_TEST_DB_NAME);
        mongoClient.dropDatabase (AYRONA_MARKETDATA_TEST_DB_NAME);

        dao = new MongoDao (mongoClient, AYRONA_TEST_DB_NAME, AYRONA_MARKETDATA_TEST_DB_NAME);

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
        expectedStrategyModel.setName ("Fatih algo");
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

        expectedStrategyModel.setBoundAccounts (Arrays.asList (new AccountBinder (expectedAccountModel.getId (),
                                                                                  AccountBinder.State.ACTIVE)));
        dao.createStrategy (expectedStrategyModel);

        List<StrategyModel> actualStrategyModelList = dao.findAllStrategies ();

        assertEquals (1, actualStrategyModelList.size ());

        StrategyModel actualStrategyModel = actualStrategyModelList.get (0);

        assertEquals (expectedStrategyModel.getId (), actualStrategyModel.getId ());
        assertEquals (expectedStrategyModel.getName (), actualStrategyModel.getName ());
        assertEquals (expectedStrategyModel.getCode (), actualStrategyModel.getCode ());

        assertEquals (1, actualStrategyModel.getBoundAccounts ().size ());
        assertEquals (expectedAccountModel.getId (), actualStrategyModel.getBoundAccounts ().get (0).getId ());

        List<AccountModel> accountModelList = dao.findBoundAccounts (actualStrategyModel.getId ());
        assertEquals (1, accountModelList.size ());

        AccountModel actualAccountModel = accountModelList.get (0);
        assertEquals (expectedAccountModel.getAccountantName (), actualAccountModel.getAccountantName ());

    }

    @Test
    public void bindAndFindBoundAccount(){
        StrategyModel expectedStrategyModel = new StrategyModel ();
        expectedStrategyModel.setName ("Fatih algo 2");
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

        dao.bindAccountToStrategy (expectedStrategyModel.getId (), expectedAccountModel.getId ());

        List<AccountModel> actualAccountList = dao.findBoundAccounts (expectedStrategyModel.getId ());
        assertEquals (1, actualAccountList.size ());

        AccountModel actualAccountModel = actualAccountList.get (0);
        assertEquals (expectedAccountModel.getAccountantName (), actualAccountModel.getAccountantName ());

    }

}