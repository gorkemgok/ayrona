package com.ayronasystems.rest;

import com.ayronasystems.core.algo.FunctionFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by gorkemgok on 26/05/16.
 */
public class AyronaBootstrap implements ServletContextListener {


    public void contextInitialized (ServletContextEvent servletContextEvent) {
        FunctionFactory.scanFunctions ();
    }

    public void contextDestroyed (ServletContextEvent servletContextEvent) {

    }
}
