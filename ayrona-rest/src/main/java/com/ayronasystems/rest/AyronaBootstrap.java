package com.ayronasystems.rest;

import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaBootstrap implements ServletContextListener {

    private static Logger log = LoggerFactory.getLogger (AyronaBootstrap.class);

    public void contextInitialized (ServletContextEvent servletContextEvent) {
        FunctionFactory.scanFunctions ();
        Symbols.init ();
        for (Symbol symbol : Symbols.getList ()){
            log.info ("Initialized symbol:{}", symbol);
        }
    }

    public void contextDestroyed (ServletContextEvent servletContextEvent) {

    }
}
