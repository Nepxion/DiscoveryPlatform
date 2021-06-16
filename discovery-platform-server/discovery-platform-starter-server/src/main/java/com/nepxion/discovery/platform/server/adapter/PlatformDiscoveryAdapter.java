package com.nepxion.discovery.platform.server.adapter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.GatewayType;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.console.resource.StrategyResource;

public class PlatformDiscoveryAdapter {
    @Value("${" + DiscoveryConstant.SPRING_APPLICATION_NAME + "}")
    private String springApplicationName;

    @Autowired
    private StrategyResource strategyResource;

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private ConfigResource configResource;

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

    public String getGroupName(String serviceName) {
        return serviceResource.getGroup(serviceName);
    }

    public RuleEntity toRuleEntity(String config) {
        return configResource.toRuleEntity(config);
    }

    public String fromRuleEntity(RuleEntity ruleEntity) {
        return configResource.fromRuleEntity(ruleEntity);
    }

    public RuleEntity getConfig(String serviceName) throws Exception {
        String group = serviceResource.getGroup(serviceName);
        return configResource.getRemoteRuleEntity(group, serviceName);
    }

    public void publishConfig(String serviceName, RuleEntity ruleEntity) throws Exception {
        String group = serviceResource.getGroup(serviceName);
        configResource.updateRemoteRuleEntity(group, serviceName, ruleEntity);
    }

    public void publishConfig(String groupName, String serviceName, String config) throws Exception {
        configResource.updateRemoteConfig(groupName, serviceName, config);
    }

    public void publishConfig(String serviceName, String config) throws Exception {
        String groupName = getGroupName(serviceName);
        publishConfig(groupName, serviceName, config);
    }

    public List<ResultEntity> viewConfig(String serviceName) {
        return configResource.viewConfig(serviceName);
    }

    public RuleEntity getLocalRuleConfig(String serviceName) {
        List<String> configList = getRuleConfig(serviceName);
        return toRuleEntity(configList.get(0));
    }

    public RuleEntity getGlobalRuleConfig(String serviceName) {
        List<String> configList = getRuleConfig(serviceName);
        return toRuleEntity(configList.get(1));
    }

    public RuleEntity getPartialRuleConfig(String serviceName) {
        List<String> configList = getRuleConfig(serviceName);
        return toRuleEntity(configList.get(2));
    }

    @SuppressWarnings("unchecked")
    private List<String> getRuleConfig(String serviceName) {
        List<ResultEntity> resultEntityList = viewConfig(serviceName);
        return JsonUtil.fromJson(resultEntityList.get(0).getResult(), List.class);
    }

    public boolean validateExpression(String expression, String validation) {
        return strategyResource.validateExpression(expression, validation);
    }
}