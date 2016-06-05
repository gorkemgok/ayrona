package com.ayronasystems.core.service.discovery;

import java.util.List;

/**
 * Created by gorkemgok on 04/06/16.
 */
public interface ServiceExplorer {

    List<String> discoverServices();

    List<ServiceInfo> discoverService(ServiceType serviceType);

}
