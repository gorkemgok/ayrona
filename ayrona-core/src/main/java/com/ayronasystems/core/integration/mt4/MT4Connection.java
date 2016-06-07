package com.ayronasystems.core.integration.mt4;

import com.jfx.Broker;

import java.io.IOException;

/**
 * Created by gorkemgok on 06/06/15.
 */
public class MT4Connection extends com.jfx.strategy.Strategy{

    private String broker;

    private String login;

    private String password;

    private String terminalHost;

    private int terminalPort;

    public MT4Connection (String broker, String login, String password, String terminalHost, int terminalPort) {
        this.broker = broker;
        this.login = login;
        this.password = password;
        this.terminalHost = terminalHost;
        this.terminalPort = terminalPort;
    }

    public void connect () throws IOException {
        super.connect (terminalHost, terminalPort, new Broker (broker), login, password);
    }

    @Override
    public boolean equals (Object obj) {
        if (obj instanceof MT4Connection){
            MT4Connection mt4Connection = (MT4Connection)obj;
            if (mt4Connection.getBroker ().equals (broker) && mt4Connection.getLogin ().equals (login) && mt4Connection.getPassword ().equals (password)){
                return true;
            }
        }
        return false;
    }

    public String getBroker () {
        return broker;
    }

    public String getLogin () {
        return login;
    }

    public String getPassword () {
        return password;
    }
}

