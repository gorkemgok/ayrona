package com.ayronasystems.core.dao.mysql;

import com.ayronasystems.core.dao.LimitOffset;
import com.ayronasystems.core.dao.PaginatedResult;
import com.ayronasystems.core.dao.mysql.model.*;
import com.ayronasystems.core.dao.mysql.result.*;
import com.ayronasystems.core.dao.mysql.result.Error;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 29/01/16.
 */
public class Tick4JDaoImpl implements Tick4JDao{

    private static Logger log = LoggerFactory.getLogger (Tick4JDaoImpl.class);

    private SessionFactory sessionFactory = null;

    private SessionFactory rateSessionFactory = null;

    private static Tick4JDao dao = new Tick4JDaoImpl ();

    public static Tick4JDao getDao(){
        return dao;
    }

    private Tick4JDaoImpl(){

    }

    public DaoResult save (BaseModel model) {
        Session session;
        BaseDao dao = BaseDao.getInstance (model.getClass ()).setSession (session = getSession (model.getClass ()));
        DaoResult result;
        try {
            dao.beginTransaction ().save (model).commit ();
            result = Successful.VALUE;
        }catch ( Exception ex ){
            log.error ("Dao Save Error on "+model.getClass (),ex);
            dao.rollback ();
            result = new Error (ex);
        }finally {
            session.close ();
        }
        return result;
    }

    public DaoResult update (BaseModel model) {
        Session session;
        DaoResult result;
        BaseDao dao = BaseDao.getInstance (model.getClass ()).setSession (session = getSession (model.getClass ()));
        try {
            dao.beginTransaction ().update (model).commit ();
            result = Successful.VALUE;
        }catch ( Exception ex ){
            log.error ("Dao Update Error on "+model.getClass (),ex);
            dao.rollback ();
            result = new Error (ex);
        }finally {
            session.close ();
        }
        return result;
    }

    public DaoResult delete (BaseModel model) {
        Session session;
        DaoResult result;
        BaseDao dao = BaseDao.getInstance (model.getClass ()).setSession (session = getSession (model.getClass ()));
        try {
            dao.beginTransaction ().delete (model).commit ();
            result = Successful.VALUE;
        }catch ( Exception ex ){
            log.error ("Dao Delete Error on "+model.getClass (),ex);
            dao.rollback ();
            result = new Error (ex);
        }finally {
            session.close ();
        }
        return result;
    }

    public PaginatedResult<StrategySessionModel> findStrategySessions (LimitOffset limitOffset) {
        Session session;
        BaseDao<StrategySessionModel> dao = BaseDao.getInstance (StrategySessionModel.class);
        dao.setSession (session = getSession());
        PaginatedResult<StrategySessionModel> result = dao.find ()
                  .orderBy ("createdate", false)
                  .listPaginated (limitOffset);
        session.close ();
        return result;
    }

    public StrategySessionModel findStrategySessionBySessionId (String sessionId) {
        Session session;
        BaseDao<StrategySessionModel> dao = BaseDao.getInstance (StrategySessionModel.class);
        dao.setSession (session = getSession ());
        StrategySessionModel result =  dao.find ().eq ("session_id", sessionId).get ();
        session.close ();
        return result;
    }

    public StrategyModel findStrategyById (int strategyId) {
        Session session;
        BaseDao<StrategyModel> dao = BaseDao.getInstance (StrategyModel.class);
        dao.setSession (session = getSession ());
        StrategyModel strategyModel = dao.findById (strategyId);
        session.close ();
        return strategyModel;
    }

    public DaoResult saveStrategy (StrategyModel strategyModel) {
        return save (strategyModel);
    }

    public PaginatedResult<StrategyModel> findStrategies (LimitOffset limitOffset, boolean dateOrderAsc) {
        Session session = getSession ();
        PaginatedResult<StrategyModel> result = BaseDao.getInstance (StrategyModel.class)
                                        .setSession (session)
                                        .find ()
                                        .orderBy ("createdate", dateOrderAsc)
                                        .listPaginated (limitOffset);
        session.close ();
        return result;
    }

    public PaginatedResult<StrategyModel> findStrategiesWithOrderBy (String orderBy, boolean dateOrderAsc, int limit, int offset) {
        Session session = getSession ();
        Dao dao = BaseDao.getInstance (StrategyModel.class);
        dao.setSession (session);
        PaginatedResult<StrategyModel> result =  dao.find ()
                      .orderBy (orderBy, dateOrderAsc)
                      .listPaginated (limit, offset);
        session.close ();
        return result;
    }

    public PaginatedResult<StrategyModel> findFavoriteStrategiesWithOrderBy (String orderBy, boolean dateOrderAsc, LimitOffset limitOffset) {
        Session session = getSession ();
        Dao dao = BaseDao.getInstance (StrategyModel.class);
        dao.setSession (session);
        PaginatedResult<StrategyModel> result =  dao.find ().eq ("is_favorite", true)
                                                    .orderBy (orderBy, dateOrderAsc)
                                                    .listPaginated (limitOffset);
        session.close ();
        return result;
    }

    public PaginatedResult<StrategyModel> findStrategiesBySessionIdWithOrderBy (String sessionId, String orderBy, boolean dateOrderAsc, int limit, int offset) {
        Session session = getSession ();
        PaginatedResult<StrategyModel> result =  BaseDao.getInstance (StrategyModel.class)
                                            .setSession (session)
                                            .find ()
                                            .eq ("session_id", sessionId)
                                            .orderBy (orderBy, dateOrderAsc)
                                            .listPaginated (limit, offset);
        session.close ();
        return result;
    }

    public DaoResult saveAccount (AccountModel accountModel) {
        return save (accountModel);
    }

    public AccountModel findAccountById (int accountId) {
        Session session;
        AccountModel result = BaseDao.getInstance (AccountModel.class).setSession (session = getSession ()).findById (accountId);
        session.close ();
        return result;
    }

    public DaoResult saveAccountStrategyPair (AccountStrategyPairModel accountStrategyPairModel) {
        BaseDao<AccountStrategyPairModel> dao = BaseDao.getInstance (AccountStrategyPairModel.class).setSession (getSession ());
        try{
            dao.beginTransaction ().save (accountStrategyPairModel).commit ();
            return new Successful ("");
        }catch ( Exception ex ){
            log.error ("Dao Error on AccountStrategyPair", ex);
            dao.rollback ();
            return new GeneralDaoResult (DaoResultType.ERROR, ex.getMessage ());
        }
    }

    public List<AccountStrategyPairModel> findAccountStrategyPairs () {
        Session session;
        List<AccountStrategyPairModel> accountStrategyPairModelList = BaseDao.getInstance (AccountStrategyPairModel.class)
               .setSession (session = getSession ())
               .findAll ();
        session.close ();
        return accountStrategyPairModelList;
    }

    public List<AccountStrategyPairModel> findAccountStrategyPairsByAccountId (String accountId) {
        Session session;
        List<AccountStrategyPairModel> accountStrategyPairModelList = BaseDao.getInstance (AccountStrategyPairModel.class)
                                                                             .setSession (session = getSession ())
                                                                             .find ().eq ("account_id", accountId).list ();
        session.close ();
        return accountStrategyPairModelList;
    }

    public List<AccountStrategyPairModel> findAccountStrategyPairsByStrategyId (String strategyId) {
        List<AccountStrategyPairModel> accountStrategyPairModelList = BaseDao.getInstance (AccountStrategyPairModel.class)
                                                                             .setSession (getSession ())
                                                                             .find ().eq ("strategy_id", strategyId).list ();
        return accountStrategyPairModelList;
    }

    public List<BarModel> findBars (Symbol symbol, Period period) {
        Session session;
        BaseDao<BarModel> dao = BaseDao.getInstance (BarModel.class);
        dao.setSession (session = getRateSession ());
        List<BarModel> result =  dao.find ().eq ("symbol", symbol)
                  .eq ("period", period)
                  .orderBy ("tick_date")
                  .list ();
        session.close ();
        return result;
    }

    public List<BarModel> findBarsBetweenDates (Symbol symbol, Period period, Date beginDate, Date endDate) {
        Session session;
        BaseDao<BarModel> dao = BaseDao.getInstance (BarModel.class);
        dao.setSession (session = getRateSession ());
        List<BarModel> result = dao.find ().eq ("symbol", symbol)
                      .eq ("period", period)
                      .gt ("tick_date", beginDate)
                      .lt ("tick_date", endDate)
                      .orderBy ("tick_date")
                      .list ();
        session.close ();
        return result;
    }

    public <E> BaseDao<E> getBaseDao (Class<E> clazz) {
        Session session = getSessionFactory (clazz).openSession ();
        return BaseDao.getInstance (clazz).setSession (session);
    }

    private SessionFactory getSessionFactory (Class entityClass) {
        if (entityClass.equals (BarModel.class)){
            return getRateSessionFactory ();
        }else{
            return getSessionFactory ();
        }
    }

    private Session getSession(Class entityClass){
        if (entityClass.equals (BarModel.class)){
            return getRateSession ();
        }else{
            return getSession ();
        }
    }

    private Session getSession(){
        SessionFactory sessionFactory = getSessionFactory ();
        Session session = null;
        if (sessionFactory != null){
            session = sessionFactory.openSession ();
            log.debug ("New session opened : {}", session);
        }
        return session;
    }

    private Session getRateSession(){
        SessionFactory sessionFactory = getRateSessionFactory ();
        Session rateSession = null;
        if (sessionFactory != null){
            rateSession = sessionFactory.openSession ();
            log.debug ("New rate session opened : {}", rateSession);
        }
        return rateSession;
    }

    private synchronized SessionFactory getSessionFactory () {
        if ( sessionFactory != null ) {
            return sessionFactory;
        }
        try {
            sessionFactory = Database.getTick4jDB ()
                                     .getSessionFactory ();
            return sessionFactory;
        } catch ( Exception ex ) {
            log.error ("Can't create SessionFactory", ex);
        }
        return null;
    }

    private synchronized SessionFactory getRateSessionFactory () {
        if ( rateSessionFactory != null ) {
            return rateSessionFactory;
        }
        try {
            rateSessionFactory = Database.getTickRateDB ()
                                     .getSessionFactory ();
            return rateSessionFactory;
        } catch ( Exception ex ) {
            log.error ("Can't create RateSessionFactory", ex);
        }
        return null;
    }

}
