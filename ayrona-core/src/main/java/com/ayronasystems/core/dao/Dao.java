package com.ayronasystems.core.dao;

import com.ayronasystems.core.batchjob.BatchJob;
import com.ayronasystems.core.dao.model.*;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
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

    boolean updateStrategy(StrategyModel strategyModel);

    boolean deleteStrategy(String strategyId);

    List<StrategyModel> findAllStrategies();

    List<StrategyModel> findActiveStrategies();

    Optional<StrategyModel> findStrategy(String id);

    void bindAccountToStrategy(String strategyId, AccountBinderModel accountBinderModel);

    void updateBoundAccount(String strategyId, AccountBinderModel accountBinderModel);

    void unboundAccount(String strategyId, String accountId);

    List<AccountModel> findBoundAccounts (String id);

    //Account
    long getAccountCount();

    List<AccountModel> findAllAccounts();

    Optional<AccountModel> findAccount(String id);

    AccountModel createAccount(AccountModel accountModel);

    void deleteAccount(String id);

    AccountModel updateAccount(AccountModel accountModel);

    List<StrategyModel> findBoundStrategies(String accountId);

    //Batchjob
    Optional<BatchJobModel> findBatchJob(String id);

    List<BatchJobModel> findAllBatchJobs();

    BatchJobModel createBatchJob(BatchJobModel batchJobModel);

    boolean updateBatchJob(String id, BatchJob.Status status);

    boolean updateBatchJob(String id, int progress);

    //Analyze Market Data
    MarketDataAnalyzeModel createMarketDataAnalyze(MarketDataAnalyzeModel marketDataAnalyzeModel);

    Optional<MarketDataAnalyzeModel> findMarketDataAnalyze(String id);

    //EDR

    EdrModel createEdr(EdrModel edrModel);

    List<EdrModel> findEdr(Date startDate, Date endDate);

    List<EdrModel> findEdr(EdrModule edrModule, Date startDate, Date endDate);

    List<EdrModel> findEdr(EdrType edrType, Date startDate, Date endDate);

    List<EdrModel> findEdrByAccountId(String accountId, Date startDate, Date endDate);

    List<EdrModel> findEdrByStrategyId(String strategyId, Date startDate, Date endDate);

    //Position

    PositionModel createPosition(PositionModel positionModel);

    boolean closePosition(PositionModel positionModel);

    List<PositionModel> findPositionsByAccountId(String accountId);

    List<PositionModel> findOpenPositionsByAccountId(String accountId);

    List<PositionModel> findPositionsByStrategyId(String strategyId);

    List<PositionModel> findOpenPositionsByStrategyId(String strategyId);

    List<PositionModel> findAllPositions();

    //MarketData

    MarketDataModel createMarketData(MarketDataModel marketDataModel);

    List<MarketDataModel> findMarketData(Symbol symbol, Period period, Date beginningDate, Date endDate);

    List<MarketDataModel> findMarketData(Symbol symbol, Period period, Date endDate, int count);

    //Market Calendar

    MarketCalendarModel createMarketCalendar (MarketCalendarModel marketCalendarModel);

    List<MarketCalendarModel> findAllMarketCalendars();

    //Optimizer session

    OptimizerSessionModel createOptimizerSession(OptimizerSessionModel optimizerSessionModel);

    Optional<OptimizerSessionModel> findOptimizerSessionById(String id);

    boolean updateOptimizerSession(OptimizerSessionModel optimizerSessionModel);

    List<OptimizerSessionModel> findWaitingOptimizerSessions ();

    PaginatedResult<OptimizerSessionModel> findOptimizerSessions (LimitOffset limitOffset);

    boolean addOptimizedCode(String sessionId, GeneratedCode generatedCode);

}
