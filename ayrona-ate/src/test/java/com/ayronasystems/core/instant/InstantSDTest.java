package com.ayronasystems.core.instant;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.configuration.ConfigurationConstants;
import com.ayronasystems.core.service.discovery.ServiceExplorer;

/**
 * Created by gorkemgok on 04/06/16.
 */
public class InstantSDTest {

    public static void main(String[] args){
        System.setProperty (ConfigurationConstants.PROP_CONSUL_HOST, "docker1.ayronasystems.com");
        System.setProperty (ConfigurationConstants.PROP_CONSUL_PORT, "8500");
        ServiceExplorer serviceExplorer = Singletons.INSTANCE.getServiceExplorer ();
        for (String service : serviceExplorer.discoverServices ()){
            System.out.println (service);
        }
    }

}
