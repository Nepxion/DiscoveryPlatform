package com.nepxion.discovery.platform.client.route.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.plugin.strategy.gateway.entity.GatewayStrategyRouteEntity;
import com.nepxion.discovery.plugin.strategy.gateway.route.GatewayStrategyRoute;

public class GatewayStrategyRouteProcessor {
    @Autowired
    private GatewayStrategyRoute gatewayStrategyRoute;

    @PostConstruct
    public void initialize() {
        List<GatewayStrategyRouteEntity> gatewayStrategyRouteEntityList = new ArrayList<>();
        gatewayStrategyRoute.updateAll(gatewayStrategyRouteEntityList);
    }
}