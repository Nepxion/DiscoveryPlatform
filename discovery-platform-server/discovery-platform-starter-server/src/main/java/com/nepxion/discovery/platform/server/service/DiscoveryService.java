package com.nepxion.discovery.platform.server.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.console.entity.GatewayType;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.plugin.framework.parser.PluginConfigDeparser;
import com.nepxion.discovery.plugin.framework.parser.PluginConfigParser;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public class DiscoveryService {
    @Value("${" + DiscoveryConstant.SPRING_APPLICATION_NAME + "}")
    private String springApplicationName;

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private ConfigResource configResource;

    @Autowired
    private PluginConfigParser pluginConfigParser;

    @Autowired
    private PluginConfigDeparser pluginConfigDeparser;

    public List<InstanceEntity> getInstanceList(String serviceName) {
        List<InstanceEntity> instanceList = serviceResource.getInstanceList(serviceName);
        instanceList.sort(Comparator.comparing(InstanceEntity::getServiceId));
        return instanceList;
    }

    public List<String> getServiceNames() {
        List<String> serviceNames = serviceResource.getServices();
        serviceNames.remove(getSpringApplicationName());
        serviceNames.removeAll(serviceResource.getGateways());
        serviceNames.sort(String::compareTo);
        return serviceNames;
    }

    public List<String> getGatewayNames() {
        List<String> gatewayNames = serviceResource.getGateways();
        gatewayNames.sort(String::compareTo);
        return gatewayNames;
    }

    public List<String> getGatewayNames(GatewayType gatewayType) {
        List<String> gatewayNames = serviceResource.getGatewayList(gatewayType);
        gatewayNames.sort(String::compareTo);
        return gatewayNames;
    }

    public String getSpringApplicationName() {
        return springApplicationName;
    }

    public String getGroup(String serviceName) {
        return serviceResource.getGroup(serviceName);
    }

    public String getRemoteConfig(String groupName, String serviceName) throws Exception {
        return configResource.getRemoteConfig(groupName, serviceName);
    }

    public void updateRemoteConfig(String groupName, String serviceName, String config) throws Exception {
        configResource.updateRemoteConfig(groupName, serviceName, config);
    }

    public RuleEntity xmlToRuleEntity(String config) {
        return pluginConfigParser.parse(config);
    }

    public String ruleEntityToXml(RuleEntity ruleEntity) {
        return pluginConfigDeparser.deparse(ruleEntity);
    }
}