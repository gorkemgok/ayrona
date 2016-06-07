package com.ayronasystems.data;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaDSBootstrap implements ServletContextListener {

    private DataServiceEngine dataServiceEngine;

    public AyronaDSBootstrap () {
        dataServiceEngine = new DataServiceEngine ();
    }

    public void contextInitialized (ServletContextEvent servletContextEvent) {
        dataServiceEngine.init ();
    }

    public void contextDestroyed (ServletContextEvent servletContextEvent) {
        dataServiceEngine.destroy ();
    }
}
