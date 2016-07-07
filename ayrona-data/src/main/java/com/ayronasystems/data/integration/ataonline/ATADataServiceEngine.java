package com.ayronasystems.data.integration.ataonline;

import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.data.Barifier;
import com.ayronasystems.data.DataServiceEngine;
import com.ayronasystems.data.listener.BarAMQSenderListener;
import com.ayronasystems.data.listener.BarDBSaverListener;
import com.ayronasystems.data.listener.BasicTickListener;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class ATADataServiceEngine implements DataServiceEngine{

    private static Configuration conf = Configuration.getInstance ();

    private static Logger log = LoggerFactory.getLogger (ATADataServiceEngine.class);

    private JMSManager jmsManager;

    private MongoClient mongoClient;

    private Thread listenerThread;

    private ATADataTCPListener tcpListener;

    public void init () {
        try {
            jmsManager = JMSManager.getManager (conf.getString (ConfKey.AMQ_URI));
            mongoClient = Singletons.INSTANCE.getMongoClient ();

            BasicTickListener tickListener = new BasicTickListener ();
            for ( Period period : Period.values ()) {
                Barifier barifier = new Barifier (period);
                barifier.addBarListener (new BarDBSaverListener (mongoClient));
                barifier.addBarListener (new BarAMQSenderListener (jmsManager));
                tickListener.addBarifier (barifier);
            }
            ATAMarketDataPayloadListener listener = new ATAMarketDataPayloadListener (tickListener);

            tcpListener = new ATADataTCPListener (
                conf.getString (ConfKey.ATA_DATA_IP),
                conf.getInteger (ConfKey.ATA_DATA_PORT),
                listener
            );

            listenerThread = new Thread (tcpListener);
            listenerThread.start ();
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
            mongoClient.close ();
        } catch ( JMSException e ) {
            log.error ("Error while destroying Data Service Engine", e);
        }
    }
}
