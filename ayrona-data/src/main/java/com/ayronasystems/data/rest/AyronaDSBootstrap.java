package com.ayronasystems.data.rest;

import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.data.DataServiceEngine;
import com.ayronasystems.data.integration.ataonline.ATADataServiceEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaDSBootstrap implements ServletContextListener {

    private static Logger log = LoggerFactory.getLogger (AyronaDSBootstrap.class);

    private DataServiceEngine dataServiceEngine;

    public AyronaDSBootstrap () {
        Symbols.init ();
        for (Symbol symbol : Symbols.getList ()){
            log.info ("Initialized symbol:{}", symbol);
        }
        //dataServiceEngine = new MT4DataServiceEngine ();
        dataServiceEngine = new ATADataServiceEngine ();
    }

    public void contextInitialized (ServletContextEvent servletContextEvent) {
        dataServiceEngine.init ();
    }

    public void contextDestroyed (ServletContextEvent servletContextEvent) {
        dataServiceEngine.destroy ();
    }
}
