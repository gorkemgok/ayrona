package com.ayronasystems.data.rest;

import com.ayronasystems.data.integration.MT4DataServiceEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaDSBootstrap implements ServletContextListener {

    private com.ayronasystems.data.DataServiceEngine DataServiceEngine;

    public AyronaDSBootstrap () {
        DataServiceEngine = new MT4DataServiceEngine ();
    }

    public void contextInitialized (ServletContextEvent servletContextEvent) {
        DataServiceEngine.init ();
    }

    public void contextDestroyed (ServletContextEvent servletContextEvent) {
        DataServiceEngine.destroy ();
    }
}
