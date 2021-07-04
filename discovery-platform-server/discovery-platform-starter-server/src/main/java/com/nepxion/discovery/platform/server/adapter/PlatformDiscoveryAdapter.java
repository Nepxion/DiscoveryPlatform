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

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.common.entity.FormatType;
import com.nepxion.discovery.common.entity.GatewayType;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.ServiceType;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.console.resource.StrategyResource;

public class PlatformDiscoveryAdapter {
    @Autowired
    private StrategyResource strategyResource;

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private ConfigResource configResource;

    public List<InstanceEntity> getInstanceList(String serviceName) {
        return serviceResource.getInstanceList(serviceName);
    }

    public List<String> getServiceNames() {
        return serviceResource.getServiceList(Collections.singletonList(ServiceType.SERVICE));
    }

    public List<String> getGatewayNames() {
        return serviceResource.getGateways();
    }

    public List<String> getGatewayNames(GatewayType gatewayType) {
        if (gatewayType == null) {
            return serviceResource.getGateways();
        }
        return serviceResource.getGatewayList(Collections.singletonList(gatewayType));
    }

    public List<String> getGroupNames() {
        return serviceResource.getGroups();
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
        String groupName = serviceResource.getGroup(serviceName);
        publishConfig(groupName, serviceName, ruleEntity);
    }

    public void publishConfig(String groupName, String serviceName, RuleEntity ruleEntity) throws Exception {
        configResource.updateRemoteRuleEntity(groupName, serviceName, ruleEntity);
    }

    public void publishConfig(String groupName, String serviceName, String config, FormatType formatType) throws Exception {
        configResource.updateRemoteConfig(groupName, serviceName, config, formatType);
    }

    public void publishConfig(String serviceName, String config, FormatType formatType) throws Exception {
        String groupName = getGroupName(serviceName);
        publishConfig(groupName, serviceName, config, formatType);
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