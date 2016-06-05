package com.ayronasystems.core.dao.mysql;

import com.ayronasystems.core.dao.mysql.model.*;
import com.ayronasystems.core.dao.mysql.result.DaoResult;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 29/01/16.
 */
public interface Tick4JDao {

    DaoResult save (BaseModel model);

    DaoResult update (BaseModel model);

    DaoResult delete (BaseModel model);


    PaginatedResult<StrategySessionModel> findStrategySessions (LimitOffset limitOffset);

    StrategySessionModel findStrategySessionBySessionId (String sessionId);

    StrategyModel findStrategyById (int strategyId);

    DaoResult saveStrategy (StrategyModel strategyModel);

    PaginatedResult<StrategyModel> findStrategies (LimitOffset limitOffset, boolean dateOrderAsc);

    PaginatedResult<StrategyModel> findStrategiesWithOrderBy (String orderBy, boolean dateOrderAsc, int limit, int offset);

    PaginatedResult<StrategyModel> findFavoriteStrategiesWithOrderBy (String orderBy, boolean dateOrderAsc, LimitOffset limitOffset);

    PaginatedResult<StrategyModel> findStrategiesBySessionIdWithOrderBy (String sessionId, String orderBy, boolean dateOrderAsc, int limit, int offset);

    DaoResult saveAccount (AccountModel accountModel);

    AccountModel findAccountById (int accountId);

    DaoResult saveAccountStrategyPair (AccountStrategyPairModel accountStrategyPairModel);

    List<AccountStrategyPairModel> findAccountStrategyPairs ();

    List<AccountStrategyPairModel> findAccountStrategyPairsByAccountId (String accountId);

    List<AccountStrategyPairModel> findAccountStrategyPairsByStrategyId (String strategyId);

    List<BarModel> findBars (Symbol symbol, Period period);

    List<BarModel> findBarsBetweenDates (Symbol symbol, Period period, Date beginDate, Date endDate);

    <E> BaseDao<E> getBaseDao (Class<E> clazz);
}
