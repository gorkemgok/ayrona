package com.ayronasystems.core.dao;

import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.dao.model.*;
import com.google.common.base.Optional;

import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
public interface Dao {

    Optional<UserModel> findUserByLogin(String login);

    //Strategy
    StrategyModel createStrategy(StrategyModel strategyModel);

    void updateStrategy(StrategyModel strategyModel);

    List<StrategyModel> findAllStrategies();

    Optional<StrategyModel> findStrategy(String id);

    void bindAccountToStrategy(String strategyId, String accountId);

    List<AccountModel> findBoundAccounts (String id);
    //Account

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
}
