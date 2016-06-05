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

    StrategyModel createStrategy(StrategyModel strategyModel);

    List<StrategyModel> findAllStrategies();

    void bindAccountToStrategy(String strategyId, String accountId);

    List<AccountModel> findAccountsByStrategyId(String id);

    List<AccountModel> findAllAccounts();

    AccountModel createAccount(AccountModel accountModel);

    Optional<BatchJobModel> findBatchJob(String id);

    List<BatchJobModel> findAllBatchJobs();

    BatchJobModel createBatchJob(BatchJobModel batchJobModel);

    void updateBatchJob(String id, BatchJob.Status status);

    void updateBatchJob(String id, int progress);

    MarketDataAnalyzeModel createMarketDataAnalyze(MarketDataAnalyzeModel marketDataAnalyzeModel);

    Optional<MarketDataAnalyzeModel> findMarketDataAnalyze(String id);
}
