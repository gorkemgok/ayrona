package com.ayronasystems.algo;

import com.ayronasystems.core.JMSDestination;
import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.AtaCustomAccount;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.AlgoStrategy;
import com.ayronasystems.core.algo.LiveBar;
import com.ayronasystems.core.concurrent.QueueRunner;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinder;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.SymbolPeriod;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.integration.mt4.MT4Account;
import com.ayronasystems.core.integration.mt4.MT4Connection;
import com.ayronasystems.core.integration.mt4.MT4ConnectionPool;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.strategy.SignalGenerator;
import com.ayronasystems.core.strategy.Strategy;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class AlgoTradingEngine {

    private static Logger log = LoggerFactory.getLogger (AlgoTradingEngine.class);

    private static Configuration conf = Configuration.getInstance ();

    private Dao dao = Singletons.INSTANCE.getDao ();

    private MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();

    private Map<SymbolPeriod, QueueRunner<Bar, Strategy<Bar>>> strategies = new HashMap<SymbolPeriod, QueueRunner<Bar, Strategy<Bar>>> ();

    public void init() throws JMSException {
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
        log.info ("Initializing STRATEGIES. Count:{}", strategyModelList.size ());
        for (StrategyModel strategyModel : strategyModelList){
            String name = strategyModel.getName ();
            Symbol symbol = strategyModel.getSymbol ();
            Period period = strategyModel.getPeriod ();

            log.info ("Initializing ALGO {}", name);
            SignalGenerator signalGenerator = Algo.createInstance (
                    strategyModel.getCode (),
                    strategyModel.getName ()
            );
            log.info ("Initialized ALGO {}", name);

            log.info ("Initializing MARKET DATA {}", name);
            long start = System.currentTimeMillis ();
            MarketData marketData = marketDataService.getOHLC (symbol, period);
            long end = System.currentTimeMillis ();
            log.info ("Initialized MARKET DATA {} - {} in {} ms", symbol, period, (end-start));

            List<AccountBindInfo> accountBindInfoList = new ArrayList<AccountBindInfo> ();
            List<AccountBinder> accountBinderList = strategyModel.getBoundAccounts ();
            for (AccountBinder accountBinder : accountBinderList){
                log.info ("Initializing account {} with {} lot", accountBinder.getId (), accountBinder.getLot ());
                Optional<AccountModel> accountModelOptional = dao.findAccount (accountBinder.getId ());
                if (accountModelOptional.isPresent ()) {
                    Account account;
                    AccountModel accountModel = accountModelOptional.get ();
                    if ( accountModel.getType () == AccountModel.Type.ATA_CUSTOM ) {
                        account = new AtaCustomAccount (
                                accountModel.getId (),
                                accountModel.getLoginDetail ()
                                            .getId ()
                        );
                        log.info ("Initialized account {}", accountModel.getAccountantName ());
                    } if ( accountModel.getType () == AccountModel.Type.ATA_CUSTOM ) {
                        MT4Connection mt4Connection = MT4ConnectionPool.getInstance ().getConnection (
                                accountModel.getLoginDetail ().getServer (),
                                accountModel.getLoginDetail ().getId (),
                                accountModel.getLoginDetail ().getPassword ()
                        );
                        account = new MT4Account (accountModel.getId (), mt4Connection);
                    }else {
                        account = new BasicAccount (accountModel.getId ());
                    }
                    accountBindInfoList.add (new AccountBindInfo (account, accountBinder.getLot ()));
                }
            }

            Strategy<Bar> strategy = new AlgoStrategy (
                    strategyModel.getId (),
                    signalGenerator,
                    marketData,
                    accountBindInfoList,
                    0,0
            );
            strategies.put (new SymbolPeriod (symbol, period),
                            new QueueRunner<Bar, Strategy<Bar>> (strategy)
            );
            strategyNames.append (name).append ("\n");
        }
        log.info ("Initialized strategies. Count {}, List : {}", strategies.size (), strategyNames.toString ());
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

    public void newBar(LiveBar liveBar){
        SymbolPeriod smi = new SymbolPeriod (liveBar.getSymbol (), liveBar.getPeriod ());
        for (Map.Entry<SymbolPeriod, QueueRunner<Bar, Strategy<Bar>>> entry : strategies.entrySet ()){
            SymbolPeriod symbolPeriod = entry.getKey ();
            if ( symbolPeriod.isSame (smi)){
                QueueRunner<Bar ,Strategy<Bar>> strategyRunner = entry.getValue ();
                Strategy<Bar> strategy = strategyRunner.unwrapRunnable ();
                try {
                    strategyRunner.put (liveBar.getBar ());
                    log.info ("Successfully executed strategy {}", strategy.getIdentifier ());
                } catch ( PrerequisiteException e ) {
                    log.error ("Cant process strategy "+strategy.getIdentifier (), e);
                }
            }
        }
    }

}
