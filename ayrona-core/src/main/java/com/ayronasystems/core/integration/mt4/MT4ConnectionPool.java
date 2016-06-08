package com.ayronasystems.core.integration.mt4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 26/12/15.
 */
public class MT4ConnectionPool {

    private static Logger log = LoggerFactory.getLogger (MT4ConnectionPool.class);

    private static MT4ConnectionPool pool;

    private String terminalHost;

    private int terminalPort;

    private List<MT4Connection> mt4Connections = new ArrayList<MT4Connection> ();

    private MT4ConnectionPool (String terminalHost, int terminalPort) {
        this.terminalHost = terminalHost;
        this.terminalPort = terminalPort;
    }

    public static MT4ConnectionPool createPool (String terminalHost, int terminalPort){
        pool = new MT4ConnectionPool (terminalHost, terminalPort);
        return pool;
    }

    public static void initializePool (String terminalHost, int terminalPort){
        createPool (terminalHost, terminalPort);
    }

    public static MT4ConnectionPool getInstance (){
        return pool;
    }

    public MT4Connection getConnection(String broker, String login, String password) {
        log.info ("Connection MT4 Terminal {} with {}", broker, login);
        for (MT4Connection mt4Connection : mt4Connections){
            if (mt4Connection.getBroker ().equals (broker) && mt4Connection.getLogin ().equals (login)){
                return mt4Connection;
            }
        }
        MT4Connection mt4Connection = new MT4Connection (broker, login, password, terminalHost, terminalPort);
        try {
            mt4Connection.connect ();
            mt4Connections.add (mt4Connection);
        } catch ( Throwable t ) { //TODO: find the cause of com.sun.xml.internal.ws.fault.SOAPFaultBuilder Noclassdeffound Error and dont catch an ERROR!
            log.error ("Cannot connect to MT4 Account. Continuing without connection", t);
        }
        return mt4Connection;

    }
}
