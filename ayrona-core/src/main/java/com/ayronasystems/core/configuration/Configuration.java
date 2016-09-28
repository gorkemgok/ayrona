package com.ayronasystems.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by gorkemgok on 29/12/15.
 */
public class Configuration {

    public static final String AYRN_CONF_ENV = "AYRN_CONF_ENV";

    private static Logger log = LoggerFactory.getLogger (Configuration.class);

    private static transient Configuration configuration = null;

    private static Object lock = new Object ();

    private Configuration () {
        String confFileName = getConfDir () + "/ayrona.properties";
        log.info ("Loading configuration from {}", confFileName);
        try {
            Properties properties = new Properties ();
            properties.load (new FileInputStream (confFileName));
            for ( Map.Entry property : properties.entrySet ()){
                String key = property.getKey ().toString ();
                String value = property.getValue ().toString ();
                System.setProperty (key, value);
                log.info ("Loaded property from configuration file, {}={}",key, value);
            }
        } catch ( FileNotFoundException e ) {
            log.warn ("Cant find configuration file : {}",e.getMessage ());
        } catch ( IOException e ) {
            log.warn ("Cant load configuration file",e);
        }

        if ( System.getProperty ("jfx_server_host") == null ) {
            String jfxHost = System.getProperty (ConfKey.MT4_JFX_HOST.getName ());
            if (jfxHost == null && ConfKey.MT4_JFX_HOST.hasEnvName ()){
                jfxHost = System.getenv (ConfKey.MT4_JFX_HOST.getEnvName ());
                jfxHost = jfxHost != null ? jfxHost : ConfKey.MT4_JFX_HOST.getDefaultValue ();
            }
            System.setProperty ("jfx_server_host", jfxHost);
        }
        if ( System.getProperty ("jfx_server_port") == null ) {
            String jfxPort = System.getProperty (ConfKey.MT4_JFX_PORT.getName ());
            if (jfxPort == null && ConfKey.MT4_JFX_PORT.hasEnvName ()){
                jfxPort = System.getenv (ConfKey.MT4_JFX_PORT.getEnvName ());
                jfxPort = jfxPort != null ? jfxPort : ConfKey.MT4_JFX_HOST.getDefaultValue ();
            }
            System.setProperty ("jfx_server_port", jfxPort);
        }
        if ( System.getProperty ("jfx_activation_key") == null ) {
            String jfxAk = System.getProperty (ConfKey.MT4_JFX_AK.getName ());
            if (jfxAk == null && ConfKey.MT4_JFX_AK.hasEnvName ()){
                jfxAk = System.getenv (ConfKey.MT4_JFX_AK.getEnvName ());
                jfxAk = jfxAk != null ? jfxAk : ConfKey.MT4_JFX_AK.getDefaultValue ();
            }
            System.setProperty ("jfx_activation_key", jfxAk);
        }
    }

    public static Configuration getInstance () {
        if (configuration == null){
            synchronized (lock){
                if (configuration == null){
                    configuration = new Configuration ();
                }
            }
        }
        return configuration;
    }

    public String getString (ConfKey property) {
        String sysProp = System.getProperty (property.getName ());
        if (sysProp == null && property.hasEnvName ()){
            sysProp = System.getenv (property.getEnvName ());
        }
        return sysProp != null ? sysProp : property.getDefaultValue ();
    }

    public int getInteger (ConfKey property) {
        String value = getString (property);
        return Integer.valueOf (value);
    }

    public String getConfDir(){
        String dir = System.getenv (AYRN_CONF_ENV);
        if (dir == null){
            dir = getString (ConfKey.CONFIG_DIR);
        }
        return dir;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder ("Configuration : ").append ("\n");
        stringBuilder.append ("\t")
                     .append ("AYRN_CONF_ENV")
                     .append (" = ")
                     .append (Configuration.getInstance ()
                                           .getConfDir ())
                     .append ("\n");
        for (ConfKey confKey : ConfKey.values ()){
            if (!confKey.equals (ConfKey.MT4_DS_LISTENER_PASSWORD)) {
                stringBuilder.append ("\t")
                             .append (confKey.name ())
                             .append (" = ")
                             .append (Configuration.getInstance ()
                                                   .getString (confKey))
                             .append ("\n");
            }
        }
        return stringBuilder.toString ();
    }
}
