package com.ayronasystems.core.dao;

import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.dao.model.*;
import com.ayronasystems.core.edr.EdrModule;
import com.ayronasystems.core.edr.EdrType;
import com.google.common.base.Optional;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public interface Dao {

    //User
    Optional<UserModel> findUserByLogin(String login);

    UserModel createUser(UserModel userModel);

    //Strategy
    StrategyStat getStrategyStat ();

    StrategyModel createStrategy(StrategyModel strategyModel);

    void updateStrategy(StrategyModel strategyModel);

    List<StrategyModel> findAllStrategies();

    Optional<StrategyModel> findStrategy(String id);

    void bindAccountToStrategy(String strategyId, String accountId);

    List<AccountModel> findBoundAccounts (String id);

    //Account
    long getAccountCount();

    List<AccountModel> findAllAccounts();

    Optional<AccountModel> findAccount(String id);

    AccountModel createAccount(AccountModel accountModel);

    void deleteAccount(String id);

    AccountModel updateAccount(AccountModel accountModel);

    //Batchjob
    Optional<BatchJobModel> findBatchJob(String id);

    List<BatchJobModel> findAllBatchJobs();

    BatchJobModel createBatchJob(BatchJobModel batchJobModel);

    void updateBatchJob(String id, BatchJob.Status status);

    void updateBatchJob(String id, int progress);

    //Analyze Market Data
    MarketDataAnalyzeModel createMarketDataAnalyze(MarketDataAnalyzeModel marketDataAnalyzeModel);

    Optional<MarketDataAnalyzeModel> findMarketDataAnalyze(String id);

    EdrModel createEdr(EdrModel edrModel);

    List<EdrModel> findEdr(EdrModule edrModule, Date startDate, Date endDate);

    List<EdrModel> findEdr(EdrType edrType, Date startDate, Date endDate);

    List<EdrModel> findEdrByAccountId(String accountId, Date startDate, Date endDate);

    List<EdrModel> findEdrByStrategyId(String strategyId, Date startDate, Date endDate);
}
