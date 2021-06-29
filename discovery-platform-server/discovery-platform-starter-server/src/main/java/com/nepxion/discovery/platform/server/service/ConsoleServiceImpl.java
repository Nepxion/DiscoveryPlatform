package com.nepxion.discovery.platform.server.service;

import com.nepxion.discovery.common.entity.ConfigType;
import com.nepxion.discovery.common.entity.DiscoveryType;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */
public class ConsoleServiceImpl implements ConsoleService {

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private ConfigResource configResource;

    @Override
    public String getDiscoveryType() {
        DiscoveryType discoveryType = serviceResource.getDiscoveryType();
        return discoveryType.getValue();
    }

    @Override
    public String getConfigType() {
        ConfigType configType = configResource.getConfigType();
        return configType.getValue();
    }

}
