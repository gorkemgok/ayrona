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

    private static Logger log = LoggerFactory.getLogger (Configuration.class);

    private static Configuration configuration = null;

    private Configuration () {
        String confFileName = getConfDir () + "/tick4j.properties";
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
            log.warn ("Cant find configuration file",e);
        } catch ( IOException e ) {
            log.warn ("Cant load configuration file",e);
        }

        if ( System.getProperty ("jfx_server_host") == null ) {
            System.setProperty ("jfx_server_host", getString (ConfKey.JFX_HOST));
        }
        if ( System.getProperty ("jfx_server_port") == null ) {
            System.setProperty ("jfx_server_port", getString (ConfKey.JFX_PORT));
        }
    }

    public static Configuration getInstance () {
        if (configuration == null){
            configuration = new Configuration ();
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
        String dir = System.getenv ("T4J_CONF");
        if (dir == null){
            dir = getString (ConfKey.CONFIG_DIR);
        }
        return dir;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder ("Configuration : ").append ("\n");
        stringBuilder.append ("\t")
                     .append ("T4J_CONF")
                     .append (" = ")
                     .append (Configuration.getInstance ()
                                           .getConfDir ())
                     .append ("\n");
        for (ConfKey confKey : ConfKey.values ()){
            if (!confKey.equals (ConfKey.LDS_TERMINAL_USER_PASSWORD)) {
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
