package com.ayronasystems.endtoend;

import com.ayronasystems.ate.AlgoTradingEngine;
import com.ayronasystems.core.JMSDestination;
import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.algo.LiveBar;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.configuration.ConfigurationConstants;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinder;
import com.ayronasystems.core.dao.model.AccountModel;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.DateUtils;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import javax.jms.JMSException;
import java.util.Arrays;

/**
 * Created by gorkemgok on 19/06/16.
 */
public class AlgoTradingEngineTestITCase {

    private static final String AYRONA_ETE_TEST_DB_NAME = "ayrona_ate_ete_test";

    private static Configuration conf = Configuration.getInstance ();

    private AlgoTradingEngine ate;

    @Before
    public void setup(){
        FunctionFactory.scanFunctions ();

        System.setProperty ("ayrona.fake.today", "01.12.2015");
        System.setProperty (ConfigurationConstants.PROP_MONGODB_DS, AYRONA_ETE_TEST_DB_NAME);

        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (AYRONA_ETE_TEST_DB_NAME);

        Dao dao = Singletons.INSTANCE.getDao ();

        AccountModel accountModel = new AccountModel ();
        accountModel.setType (AccountModel.Type.DUMMY);
        accountModel.setAccountantName ("Görkem Gök ETE");
        dao.createAccount (accountModel);

        AccountModel accountModel2 = new AccountModel ();
        accountModel2.setType (AccountModel.Type.DUMMY);
        accountModel2.setAccountantName ("Fatih Altıdiş ETE");
        dao.createAccount (accountModel2);

        AccountModel accountModel3 = new AccountModel ();
        accountModel3.setType (AccountModel.Type.DUMMY);
        accountModel3.setAccountantName ("Onur Altıdiş ETE");
        dao.createAccount (accountModel3);

        StrategyModel strategyModel = new StrategyModel ();
        strategyModel.setSymbol (Symbols.of("TEST"));
        strategyModel.setPeriod (Period.M5);
        strategyModel.setName ("ATE_ETE_TEST");
        strategyModel.setCode ("var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                                       "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                                       "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                                       "Sistem.SELL = Sistem.LT(SMA_5, SMA_20);");
        strategyModel.setAccounts (Arrays.asList (
                new AccountBinder (accountModel.getId (), AccountBinder.State.ACTIVE, 1),
                new AccountBinder (accountModel2.getId (), AccountBinder.State.ACTIVE, 1)));
        dao.createStrategy (strategyModel);

        StrategyModel strategyModel2 = new StrategyModel ();
        strategyModel2.setSymbol (Symbols.of("TEST"));
        strategyModel2.setPeriod (Period.M5);
        strategyModel2.setName ("ATE_ETE_TEST 2");
        strategyModel2.setCode ("var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                                       "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                                       "Sistem.BUY = Sistem.LT(SMA_5, SMA_20);" +
                                       "Sistem.SELL = Sistem.GT(SMA_5, SMA_20);");
        strategyModel2.setAccounts (Arrays.asList (
                new AccountBinder (accountModel2.getId (), AccountBinder.State.ACTIVE, 1),
                new AccountBinder (accountModel3.getId (), AccountBinder.State.ACTIVE, 1)));
        dao.createStrategy (strategyModel2);

        ate = new AlgoTradingEngine ();
        try {
            ate.init ();
        } catch ( JMSException e ) {
            e.printStackTrace ();
        }
    }

    @Test
    public void simulateTest(){
        JMSManager jmsManager = JMSManager.getManager (conf.getString (ConfKey.AMQ_URI));
        MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();
        MarketData marketData = marketDataService.getOHLC (Symbols.of("TEST"), Period.M5,
                                                           DateUtils.parseDate ("01.12.2015 00:00:00"));
        System.out.println ("Loaded market data for producer, "+marketData.size ());
        for ( Moment moment : marketData){
            LiveBar liveBar = new LiveBar (Symbols.of("TEST"), Period.M5, (Bar)moment);
            try {
                jmsManager.getWorker ().destination (JMSDestination.BARS).send (liveBar);
                System.out.println ("Send bar:"+liveBar.getBar ());
                Thread.currentThread ().sleep (500);
            } catch ( Exception e ) {
                e.printStackTrace ();
            }

        }

    }

}
