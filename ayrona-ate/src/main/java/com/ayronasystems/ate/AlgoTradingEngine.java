package com.ayronasystems.ate;

import com.ayronasystems.core.edr.Edr;
import com.ayronasystems.core.jms.JMSDestination;
import com.ayronasystems.core.jms.JMSManager;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBinder;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.AlgoStrategy;
import com.ayronasystems.core.algo.LiveBar;
import com.ayronasystems.core.concurrent.QueueRunner;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinderModel;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.integration.mt4.MT4ConnectionPool;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.strategy.SPStrategy;
import com.ayronasystems.core.strategy.Strategy;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.util.DateUtils;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class AlgoTradingEngine {

    public static final AlgoTradingEngine INSTANCE = new AlgoTradingEngine ();

    private static final long A_WEEK_IN_MILLIS = 1000 * 60 *60 *24 * 7;

    private Date prevWeek;

    private static Logger log = LoggerFactory.getLogger (AlgoTradingEngine.class);

    private static Configuration conf = Configuration.getInstance ();

    private Dao dao = Singletons.INSTANCE.getDao ();

    private MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();

    private List<QueueRunner<Bar, SPStrategy<Bar>>> strategyRunners = new ArrayList<QueueRunner<Bar, SPStrategy<Bar>>> ();

    public void init() throws JMSException {
        Date now;
        //for test purpose
        String fakeToday = System.getProperty ("ayrona.fake.today");
        if (fakeToday != null){
            now = DateUtils.parseDate (fakeToday+" 00:00:00");
        }else{
            now = new Date();
        }

        prevWeek = new Date(now.getTime () - A_WEEK_IN_MILLIS);

        MT4ConnectionPool.initializePool (
                conf.getString (ConfKey.MT4_TERMINAL_HOST),
                conf.getInteger (ConfKey.MT4_TERMINAL_PORT)
        );

        initializeAMQ ();

        initializeStrategies ();
    }

    private void initializeStrategies(){
        StringBuilder strategyNames = new StringBuilder ();
        List<StrategyModel> strategyModelList = dao.findAllStrategies ();
        log.info ("Initializing strategies... Count:{}", strategyModelList.size ());
        for (StrategyModel strategyModel : strategyModelList){
            if (strategyModel.getState ().equals (AccountBinderModel.State.ACTIVE)) {
                initializeStrategy (strategyModel);
                strategyNames.append (strategyModel.getName ())
                             .append (",");
            }
        }
        log.info ("Initialized Strategy Runners. Count {}, List : {}", strategyRunners.size (), strategyNames.toString ());
    }

    private void initializeStrategy(StrategyModel strategyModel){
        String name = strategyModel.getName ();
        Symbol symbol = strategyModel.getSymbol ();
        Period period = strategyModel.getPeriod ();

        Algo algo = Algo.createInstance (
                strategyModel.getCode (),
                strategyModel.getName ()
        );
        log.info ("Initialized ate {}", name);

        long start = System.currentTimeMillis ();

        int initialBarCount = Math.max (strategyModel.getInitialBarCount (), algo.getNeededInputCount());
        MarketData initialMarketData = marketDataService.getOHLC (symbol, period, new Date(), initialBarCount);
        long end = System.currentTimeMillis ();
        log.info ("Loaded OHLC data {} - {} with {} price in {} ms", symbol, period, initialMarketData.size() , (end-start));

        List<AccountBinder> accountBinderList = getAccountBindInfoList (strategyModel);

        SPStrategy<Bar> strategy = new AlgoStrategy (
                true,
                initialBarCount,
                strategyModel.getId (),
                algo,
                initialMarketData,
                accountBinderList,
                0,0
        );
        QueueRunner<Bar, SPStrategy<Bar>> strategyRunner = new QueueRunner<Bar, SPStrategy<Bar>> (strategy);
        strategyRunner.start ();
        synchronized (this) {
            strategyRunners.add (strategyRunner);
        }
        Edr.startStrategy ().strategy (strategy).success ().putQueue ();
        log.info ("Initialized strategy {} with {} accounts", name, accountBinderList.size ());
    }

    private List<AccountBinder> getAccountBindInfoList(StrategyModel strategyModel){
        List<AccountBinderModel> accountBinderModelList = strategyModel.getAccounts ();
        List<AccountBinder> accountBinderList = new ArrayList<AccountBinder> ();
        for (AccountBinderModel accountBinderModel : accountBinderModelList ){
            if ( accountBinderModel.getState ().equals (AccountBinderModel.State.ACTIVE)) {
                Optional<AccountModel> accountModelOptional = dao.findAccount (accountBinderModel.getId ());
                if ( accountModelOptional.isPresent () ) {
                    AccountModel accountModel = accountModelOptional.get ();
                    Account account = new BasicAccount (accountModel.getId (), accountModel.getAccountantName (),
                                                        BasicAccount.createAccountRemoteUsing (accountModel));
                    accountBinderList.add (new AccountBinder (account, accountBinderModel.getLot ()));
                    log.info ("Bound account {} with {} lot to {}", accountModel.getAccountantName (),
                              accountBinderModel.getLot (), strategyModel.getName ());
                }
            }
        }
        return accountBinderList;
    }

    private void initializeAMQ() throws JMSException{
        //Initialize AMQ Bar Listener
        log.info ("Initializing live data source {}",conf.getString (ConfKey.AMQ_URI));
        JMSManager jmsManager = JMSManager.getManager (conf.getString (ConfKey.AMQ_URI));
        jmsManager.getConsumer (JMSDestination.BARS, null).setMessageListener (new MessageListener () {
            public void onMessage (Message message) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                try {
                    LiveBar liveBar = (LiveBar) objectMessage.getObject ();
                    newBar (liveBar);
                } catch ( JMSException e ) {
                    log.error ("Cant trigger engine with new bar.", e);
                }
            }
        });
    }

    public synchronized void newBar(LiveBar liveBar){
        for (int i=0; i < strategyRunners.size ();i++){
            QueueRunner<Bar, SPStrategy<Bar>> strategyRunner = strategyRunners.get (i);
            if (strategyRunner != null) {
                SPStrategy<Bar> strategy = strategyRunner.unwrapRunnable ();
                if ( strategy.getSymbolPeriod ()
                             .equals (liveBar.getSymbolPeriod ()) ) {
                    try {
                        strategyRunner.put (liveBar.getBar ());
                        log.info ("Successfully executed strategy {} at {}-{}", strategy.getName (),
                                  strategy.getSymbolPeriod ()
                                          .getSymbol (), strategy.getSymbolPeriod ()
                                                                 .getPeriod ());
                    } catch ( PrerequisiteException e ) {
                        log.error ("Cant process strategy " + strategy.getId (), e);
                    }
                }
            }
        }
    }

    public void syncStrategies(){
        log.info ("Synchronization started...");
        List<StrategyModel> strategyModelList = dao.findActiveStrategies ();
        //Process of register strategy
        for ( StrategyModel strategyModel : strategyModelList){
            boolean exists = false;
            for (QueueRunner<Bar, SPStrategy<Bar>> strategyRunner : strategyRunners){
                SPStrategy<Bar> strategy = strategyRunner.unwrapRunnable ();
                if (strategyModel.getId ().equals (strategy.getId ())){
                    exists = true;
                    syncAccountBinders (strategyModel, strategy);
                    break;
                }
            }
            if (!exists){
                registerStrategy (strategyModel);
            }
        }
        //Process of deregister strategy
        for (int i = 0; i < strategyRunners.size (); i++){
            SPStrategy<Bar> strategy = strategyRunners.get (i).unwrapRunnable ();
            boolean exists = false;
            for ( StrategyModel strategyModel : strategyModelList) {
                if (strategyModel.getId ().equals (strategy.getId ())){
                    exists = true;
                    break;
                }
            }
            if (!exists){
                deregisterStrategy (strategy.getId ());
                i--;
            }
        }
    }

    private void syncAccountBinders(StrategyModel strategyModel, Strategy strategy){
        List<AccountBinder> accountBinderList = strategy.getAccountBinderList ();
        List<AccountBinderModel> accountBinderModelList = strategyModel.getAccounts ();
        //Process for register account
        for (AccountBinderModel accountBinderModel : accountBinderModelList ){
            if ( accountBinderModel.getState ().equals (AccountBinderModel.State.ACTIVE)){
                boolean exists = false;
                for (AccountBinder accountBinder : accountBinderList ) {
                    if ( accountBinderModel.getId ().equals (accountBinder.getAccount ().getId ())){
                        exists = true;
                        double currentLot = accountBinder.getLot ();
                        if (currentLot != accountBinderModel.getLot ()) {
                            accountBinder.setLot (accountBinderModel.getLot ());
                            log.info ("Synchronized lot of account {} to {} from {}",
                                      accountBinder.getAccount ().getName (),
                                      accountBinderModel.getLot (),
                                      currentLot);
                        }
                    }
                }
                if (!exists){
                    Optional<AccountModel> accountModelOptional = dao.findAccount (accountBinderModel.getId ());
                    if (accountModelOptional.isPresent ()) {
                        AccountModel accountModel = accountModelOptional.get ();
                        Account account = new BasicAccount (accountModel.getId (), accountModel.getAccountantName (),
                                                            BasicAccount.createAccountRemoteUsing (accountModel));
                        AccountBinder accountBinder = new AccountBinder (account, accountBinderModel.getLot ());
                        strategy.registerAccount (accountBinder);
                        Edr.startAccount ().account (account).lot (accountBinder.getLot ()).success ().putQueue ();
                        log.info ("Registered account {} to strategy {} with {} lot(s)", account.getName (),
                                  strategy.getName (),
                                  accountBinder.getLot ());
                    }
                }
            }
        }
        //process for deregister account
        for ( int i = 0; i < accountBinderList.size (); i++){
            boolean exists = false;
            Account account = accountBinderList.get (i).getAccount ();
            for (AccountBinderModel accountBinderModel : accountBinderModelList){
                if (accountBinderModel.getState ().equals (AccountBinderModel.State.ACTIVE) && accountBinderModel.getId ().equals (account.getId ())){
                    exists = true;
                    break;
                }
            }
            if (!exists){
                strategy.deregisterAccount (account.getId ());
                Edr.stopAccount ().account (account).success ().putQueue ();
                log.info ("Deregistered account {} to strategy {}", account.getName (),
                          strategy.getName ());
            }
        }
    }

    public void registerStrategy(StrategyModel strategyModel){
        initializeStrategy (strategyModel);
    }

    public synchronized void deregisterStrategy(String strategyId){
        for (int i = 0; i < strategyRunners.size (); i++){
            SPStrategy<Bar> strategy = strategyRunners.get (i).unwrapRunnable ();
            if (strategy.getId ().equals (strategyId)){
                strategyRunners.remove (i);
                Edr.stopStrategy ().strategy (strategy).success ().putQueue ();
                log.info ("Deregistered strategy {}", strategy.getName ());
                break;
            }
        }
    }

    public List<RunningStrategy> getRunningStrategyList () {
        List<RunningStrategy> runningStrategyList = new ArrayList<RunningStrategy> ();
        for (int i = 0; i < strategyRunners.size (); i++){
            SPStrategy<Bar> strategy = strategyRunners.get (i).unwrapRunnable ();
            runningStrategyList.add ( new RunningStrategy (strategy) );
        }
        return runningStrategyList;
    }

    public synchronized boolean bindAccount(String strategyId, Account account, double lot){
        Iterator<QueueRunner<Bar, SPStrategy<Bar>>> iterator = strategyRunners.iterator ();
        while (iterator.hasNext ()){
            QueueRunner<Bar, SPStrategy<Bar>> strategyRunner = iterator.next ();
            SPStrategy<Bar> spStrategy = strategyRunner.unwrapRunnable ();
            if (spStrategy.getId ().equals (strategyId)){
                AccountBinder accountBinder = new AccountBinder (account, lot);
                spStrategy.registerAccount (accountBinder);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean unbindAccount(String strategyId, String accountId){
        Iterator<QueueRunner<Bar, SPStrategy<Bar>>> iterator = strategyRunners.iterator ();
        while (iterator.hasNext ()){
            QueueRunner<Bar, SPStrategy<Bar>> strategyRunner = iterator.next ();
            SPStrategy<Bar> spStrategy = strategyRunner.unwrapRunnable ();
            if (spStrategy.getId ().equals (strategyId)){
                spStrategy.deregisterAccount (accountId);
                return true;
            }
        }
        return false;
    }

    public void destroy(){
        //TODO: Destroy ATE
    }

}
