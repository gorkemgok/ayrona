package com.ayronasystems.data.rest;

import com.ayronasystems.data.DataServiceEngine;
import com.ayronasystems.data.integration.ataonline.ATADataServiceEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaDSBootstrap implements ServletContextListener {

    private DataServiceEngine dataServiceEngine;

    public AyronaDSBootstrap () {
        //DataServiceEngine = new MT4DataServiceEngine ();
        dataServiceEngine = new ATADataServiceEngine ();
    }

    public void contextInitialized (ServletContextEvent servletContextEvent) {
        dataServiceEngine.init ();
    }

    public void contextDestroyed (ServletContextEvent servletContextEvent) {
        dataServiceEngine.destroy ();
    }
}
