package com.ayronasystems.core.service.discovery;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.model.CatalogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gorkemgok on 04/06/16.
 */
public class ConsulServiceExplorer implements ServiceExplorer {

    private ConsulClient consulClient;

    public ConsulServiceExplorer (String consulHost, int consulPort) {
        consulClient = new ConsulClient (consulHost, consulPort);
    }

    public List<String> discoverServices () {
        Set<Map.Entry<String, List<String>>> entrySet = consulClient.getCatalogServices (null).getValue ().entrySet ();
        List<String> response = new ArrayList<String>(entrySet.size ());
        for (Map.Entry<String, List<String>> entry : entrySet){
            response.add (entry.getKey ());
        }
        return response;
    }

    public List<ServiceInfo> discoverService (ServiceType serviceType) {
        Response<List<CatalogService>> response = consulClient.getCatalogService (serviceType.getName (), null);
        List<CatalogService> catalogServiceList = response.getValue ();
        List<ServiceInfo> serviceInfoList = new ArrayList<ServiceInfo> (catalogServiceList.size ());
        for (CatalogService catalogService : catalogServiceList){
            serviceInfoList.add (new ServiceInfo (catalogService.getAddress (), catalogService.getServicePort ()));
        }
        return serviceInfoList;
    }
}
