package com.ayronasystems.data;

import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.data.integration.ataonline.ATADataServiceEngine;
import com.ayronasystems.data.integration.mt4.MT4DataServiceEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ayronasystems.core.configuration.ConfigurationConstants.*;
/**
 * Created by gorkemgok on 07/06/16.
 */
public class Main {

    private static Logger log = LoggerFactory.getLogger (Main.class);

    private static Configuration conf;

    public static void main(String [] args){
        System.setProperty (PROP_MT4_DS_LISTENER_BROKER, "AtaOnline-Demo");
        System.setProperty (PROP_MT4_DS_LISTENER_LOGIN, "1218368283");
        System.setProperty (PROP_MT4_DS_LISTENER_PASSWORD, "gm7xtnn");
        System.setProperty (PROP_MT4_TERMINAL_HOST, "104.197.70.53");
        System.setProperty (PROP_MT4_TERMINAL_PORT, "7788");
        System.setProperty (PROP_MT4_JFX_HOST, "85.96.170.210");
        System.setProperty (PROP_MT4_JFX_PORT, "7790");

        conf = Configuration.getInstance ();

        System.out.println (conf.toString ());

        MT4DataServiceEngine MT4DataServiceEngine = new MT4DataServiceEngine ();
        //MT4DataServiceEngine.init ();

        ATADataServiceEngine ataDataServiceEngine = new ATADataServiceEngine ();
        ataDataServiceEngine.init ();

    }

}
