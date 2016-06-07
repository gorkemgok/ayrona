package com.ayronasystems.data;

import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.integration.mt4.MT4Connection;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class DataServiceEngine {

    private static Logger log = LoggerFactory.getLogger (DataServiceEngine.class);

    private static Configuration conf = Configuration.getInstance ();

    private JMSManager jmsManager;

    private MongoClient mongoClient;

    private MT4Connection mt4ListenerConnection;

    public void init(){
        log.info (conf.toString ());

        System.setProperty ("jfx_server_host", conf.getString (ConfKey.MT4_JFX_HOST));
        System.setProperty ("jfx_server_port", conf.getString (ConfKey.MT4_JFX_PORT));

        jmsManager = JMSManager.getManager (conf.getString (ConfKey.AMQ_URI));
        mongoClient = Singletons.INSTANCE.getMongoClient ();

        MT4BulkTickListener mt4BulkTickListener = new MT4BulkTickListener ();
        Barifier barifier = new Barifier ();
        barifier.addBarListener (new BarDBSaverListener (mongoClient));
        barifier.addBarListener (new BarAMQSenderListener (jmsManager));
        mt4BulkTickListener.addBarifier (barifier);
        mt4ListenerConnection = new MT4Connection (
                conf.getString (ConfKey.MT4_DS_LISTENER_BROKER),
                conf.getString (ConfKey.MT4_DS_LISTENER_LOGIN),
                conf.getString (ConfKey.MT4_DS_LISTENER_PASSWORD),
                conf.getString (ConfKey.MT4_TERMINAL_HOST),
                conf.getInteger (ConfKey.MT4_TERMINAL_PORT)
        );
        mt4ListenerConnection.setBulkTickListener (mt4BulkTickListener);
        try {
            log.info ("Establishing listener MT4 connection...");
            mt4ListenerConnection.setReconnect (true);
            mt4ListenerConnection.connect ();
            log.info ("Connected MT4 listener account");
        } catch ( IOException e ) {
            log.error ("Can't connect to mt4 lister account", e);
        }
    }

    public void destroy(){
        if (jmsManager != null){
            try {
                jmsManager.close ();
            } catch ( JMSException e ) {
                log.error ("Cant close jms manager", e);
            }
        }
        if (mongoClient != null) {
            mongoClient.close ();
        }
        if (mt4ListenerConnection != null){
            try {
                mt4ListenerConnection.close ();
            } catch ( IOException e ) {
                log.error ("Can't close MT4 connection", e);
            }
        }
        log.info ("Data Service Shutdown.It is over :(");
    }

}
