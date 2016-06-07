package com.ayronasystems.core.algo;

import com.ayronasystems.core.JMSDestination;
import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.AtaCustomAccount;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinder;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.PrerequisiteException;
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

    private static class StrategyMarketInfo{
        private Symbol symbol;

        private Period period;

        public StrategyMarketInfo (Symbol symbol, Period period) {
            this.symbol = symbol;
            this.period = period;
        }

        public Symbol getSymbol () {
            return symbol;
        }

        public Period getPeriod () {
            return period;
        }

        public boolean isSame (Object o) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass () != o.getClass () ) {
                return false;
            }

            StrategyMarketInfo that = (StrategyMarketInfo) o;

            if ( symbol != that.symbol ) {
                return false;
            }
            return period == that.period;

        }

    }

    private Dao dao = Singletons.INSTANCE.getDao ();

    private Map<StrategyMarketInfo, Strategy<Bar>> strategies = new HashMap<StrategyMarketInfo, Strategy<Bar>> ();

    public void init() throws JMSException {
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

        List<StrategyModel> strategyModelList = dao.findAllStrategies ();
        log.info ("Initializing strategies. Count:{}", strategyModelList.size ());
        for (StrategyModel strategyModel : strategyModelList){
            log.info ("Initializing algo {}", strategyModel.getName ());
            SignalGenerator signalGenerator = Algo.createInstance (
                    strategyModel.getCode (),
                    strategyModel.getName ()
            );

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
                    } else {
                        account = new BasicAccount (accountModel.getId ());
                    }
                    accountBindInfoList.add (new AccountBindInfo (account, accountBinder.getLot ()));
                }
            }

            Strategy<Bar> strategy = new AlgoStrategy (
                    strategyModel.getId (),
                    signalGenerator,
                    null,
                    accountBindInfoList,
                    0,0
            );
            strategies.put (new StrategyMarketInfo (strategyModel.getSymbol (), strategyModel.getPeriod ()),
                            strategy
            );
            log.info ("Initialized algo {}", strategyModel.getName ());
        }
        log.info ("Initialized strategies. Count {}", strategies.size ());
    }

    public void newBar(LiveBar liveBar){
        StrategyMarketInfo smi = new StrategyMarketInfo (liveBar.getSymbol (), liveBar.getPeriod ());
        for (Map.Entry<StrategyMarketInfo, Strategy<Bar>> entry : strategies.entrySet ()){
            StrategyMarketInfo strategyMarketInfo = entry.getKey ();
            if (strategyMarketInfo.isSame (smi)){
                Strategy<Bar> strategy = entry.getValue ();
                try {
                    strategy.process (liveBar.getBar ());
                    log.info ("Successfully executed strategy {}", strategy.getIdentifier ());
                } catch ( PrerequisiteException e ) {
                    log.error ("Cant process strategy "+strategy.getIdentifier (), e);
                }
            }
        }
    }

}
