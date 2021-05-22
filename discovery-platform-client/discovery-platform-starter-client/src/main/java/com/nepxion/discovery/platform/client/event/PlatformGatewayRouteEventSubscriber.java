package com.nepxion.discovery.platform.client.event;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.google.common.eventbus.Subscribe;
import com.nepxion.discovery.plugin.strategy.gateway.event.GatewayStrategyRouteAddedEvent;
import com.nepxion.discovery.plugin.strategy.gateway.event.GatewayStrategyRouteDeletedEvent;
import com.nepxion.discovery.plugin.strategy.gateway.event.GatewayStrategyRouteModifiedEvent;
import com.nepxion.discovery.plugin.strategy.gateway.event.GatewayStrategyRouteUpdatedAllEvent;
import com.nepxion.eventbus.annotation.EventBus;

@EventBus
public class PlatformGatewayRouteEventSubscriber {
    @Subscribe
    public void onGatewayStrategyRouteAdded(GatewayStrategyRouteAddedEvent gatewayStrategyRouteAddedEvent) {
        System.out.println("::::: 推送网关路由变更信息给钉钉，增加网关路由=" + gatewayStrategyRouteAddedEvent.getGatewayStrategyRouteEntity());
    }

    @Subscribe
    public void onGatewayStrategyRouteModified(GatewayStrategyRouteModifiedEvent gatewayStrategyRouteModifiedEvent) {        
        System.out.println("::::: 推送网关路由变更信息给钉钉，修改网关路由=" + gatewayStrategyRouteModifiedEvent.getGatewayStrategyRouteEntity());
    }

    @Subscribe
    public void onGatewayStrategyRouteDeleted(GatewayStrategyRouteDeletedEvent gatewayStrategyRouteDeletedEvent) {
        System.out.println("::::: 推送网关路由变更信息给钉钉，删除网关路由=" + gatewayStrategyRouteDeletedEvent.getRouteId());
    }

    @Subscribe
    public void onGatewayStrategyRouteUpdatedAll(GatewayStrategyRouteUpdatedAllEvent gatewayStrategyRouteUpdatedAllEvent) {       
        System.out.println("::::: 推送网关路由变更信息给钉钉，更新全部网关路由=" + gatewayStrategyRouteUpdatedAllEvent.getGatewayStrategyRouteEntityList());
    }
}