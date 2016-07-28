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
        System.setProperty (PROP_MT4_JFX_AK, "2209463296");
        System.setProperty (PROP_MT4_DS_LISTENER_BROKER, "AtaOnline-Demo");
        System.setProperty (PROP_MT4_DS_LISTENER_LOGIN, "1218368283");
        System.setProperty (PROP_MT4_DS_LISTENER_PASSWORD, "gm7xtnn");
        System.setProperty (PROP_MT4_TERMINAL_HOST, "104.197.70.53");
        System.setProperty (PROP_MT4_TERMINAL_PORT, "7788");
        System.setProperty (PROP_MT4_JFX_HOST, "78.190.226.5");
        System.setProperty (PROP_MT4_JFX_PORT, "7790");
        System.setProperty (PROP_SYMBOLS, "FX_EURUSD-EURUSD,FX_USDTRY-USDTRY,VOB_XAUTRY-F_XAUTRYM0816S0,VOB_X30-F_XU0300816S0");

        conf = Configuration.getInstance ();

        System.out.println (conf.toString ());

        MT4DataServiceEngine mt4DataServiceEngine = new MT4DataServiceEngine ();
        //mt4DataServiceEngine.init ();

        ATADataServiceEngine ataDataServiceEngine = new ATADataServiceEngine ();
        ataDataServiceEngine.init ();
        while ( true ){

        }

    }

}
