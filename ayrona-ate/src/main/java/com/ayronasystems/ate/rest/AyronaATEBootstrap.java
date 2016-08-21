package com.ayronasystems.ate.rest;

import com.ayronasystems.ate.AlgoTradingEngine;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaATEBootstrap implements ServletContextListener {

    private static final long SYNC_PERIOD = 1000 * 60;

    private static Logger log = LoggerFactory.getLogger (AyronaATEBootstrap.class);

    public void contextInitialized (ServletContextEvent servletContextEvent) {
        try {
            FunctionFactory.scanFunctions ();
            Symbols.init ();

            for (Symbol symbol : Symbols.getList ()){
                log.info ("Initialized symbol:{}", symbol);
            }

            AlgoTradingEngine.INSTANCE.init ();

            Timer timer = new Timer (true);
            timer.schedule (new TimerTask () {
                @Override
                public void run () {
                    AlgoTradingEngine.INSTANCE.syncStrategies ();
                }
            }, SYNC_PERIOD, SYNC_PERIOD);
            log.info ("Strategy sync is scheduled.");

        } catch ( Exception e ) {
            log.error ("AlgoTrading Engine initialization error", e);
        }
    }

    public void contextDestroyed (ServletContextEvent servletContextEvent) {
        AlgoTradingEngine.INSTANCE.destroy ();
    }
}
