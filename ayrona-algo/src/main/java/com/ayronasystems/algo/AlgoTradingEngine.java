package com.ayronasystems.algo;

import com.ayronasystems.core.JMSDestination;
import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
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
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.integration.mt4.MT4ConnectionPool;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.strategy.SPStrategy;
import com.ayronasystems.core.strategy.SignalGenerator;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class AlgoTradingEngine {

    private static Logger log = LoggerFactory.getLogger (AlgoTradingEngine.class);

    private static Configuration conf = Configuration.getInstance ();

    private Dao dao = Singletons.INSTANCE.getDao ();

    private MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();

    private List<QueueRunner<Bar, SPStrategy<Bar>>> strategyRunners = new ArrayList<QueueRunner<Bar, SPStrategy<Bar>>> ();

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
                    AccountModel accountModel = accountModelOptional.get ();
                    Account account = new BasicAccount(accountModel.getId(),
                            BasicAccount.createUsing(accountModel));
                    accountBindInfoList.add (new AccountBindInfo (account, accountBinder.getLot ()));
                }
            }

            SPStrategy<Bar> strategy = new AlgoStrategy (
                    strategyModel.getId (),
                    signalGenerator,
                    marketData,
                    accountBindInfoList,
                    0,0
            );
            strategyRunners.add (new QueueRunner<Bar, SPStrategy<Bar>> (strategy));
            strategyNames.append (name).append ("\n");
        }
        log.info ("Initialized strategyRunners. Count {}, List : {}", strategyRunners.size (), strategyNames.toString ());
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
        for (QueueRunner<Bar, SPStrategy<Bar>> strategyRunner : strategyRunners){
            SPStrategy<Bar> strategy = strategyRunner.unwrapRunnable ();
            if ( strategy.getSymbolPeriod ().equals (liveBar.getSymbolPeriod ())){
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
