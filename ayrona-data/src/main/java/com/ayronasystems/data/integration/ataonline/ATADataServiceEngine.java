package com.ayronasystems.data.integration.ataonline;

import com.ayronasystems.core.jms.JMSManager;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.configuration.HostPort;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.data.Barifier;
import com.ayronasystems.data.DataServiceEngine;
import com.ayronasystems.data.listener.BarAMQSenderListener;
import com.ayronasystems.data.listener.BarDBSaverListener;
import com.ayronasystems.data.listener.BasicTickListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class ATADataServiceEngine implements DataServiceEngine{

    private static Configuration conf = Configuration.getInstance ();

    private static Logger log = LoggerFactory.getLogger (ATADataServiceEngine.class);

    private JMSManager jmsManager;

    private Thread listenerThread;

    private ATADataTCPListener tcpListener;

    public void init () {
        try {
            jmsManager = JMSManager.getManager (conf.getString (ConfKey.AMQ_URI));

            BasicTickListener tickListener = new BasicTickListener ();
            for ( Period period : Period.values ()) {
                Barifier barifier = new Barifier (period);
                barifier.addBarListener (new BarDBSaverListener ());
                barifier.addBarListener (new BarAMQSenderListener (jmsManager));
                tickListener.addBarifier (barifier);
            }
            ATAMarketDataListener listener = new ATAMarketDataListener (tickListener);

            List<HostPort> hostPortList = new ArrayList<HostPort> ();
            for (String hpString : conf.getString (ConfKey.ATA_DATA_HPLIST).split (",")){
                hostPortList.add (new HostPort (hpString));
            }
            tcpListener = new ATADataTCPListener (hostPortList, listener);
            tcpListener.setRetryCount (conf.getInteger (ConfKey.ATA_DATA_RETRY_COUNT));
            tcpListener.setRetryWaitInSeconds (conf.getInteger (ConfKey.ATA_DATA_RETRY_WAIT));

            listenerThread = new Thread (tcpListener);
            listenerThread.start ();
            log.info ("ATADataService initialized");
        }catch ( Exception e ){
            log.error ("Error while initializing ATADataServiceEngine", e);
        }
    }

    public void destroy () {
        tcpListener.stop ();
        try {
            Thread.currentThread ().sleep (500);
        } catch ( InterruptedException e ) {
            log.error ("Error while waiting for disconnection", e);
        }
        tcpListener.disconnect ();
        try {
            jmsManager.close ();
        } catch ( JMSException e ) {
            log.error ("Error while destroying Data Service Engine", e);
        }
        log.info ("ATADataService destroyed");
    }
}
