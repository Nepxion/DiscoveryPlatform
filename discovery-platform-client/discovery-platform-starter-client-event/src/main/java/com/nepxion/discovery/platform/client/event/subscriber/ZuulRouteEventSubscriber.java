package com.nepxion.discovery.platform.client.event.subscriber;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.google.common.eventbus.Subscribe;
import com.nepxion.discovery.plugin.strategy.zuul.event.ZuulStrategyRouteAddedEvent;
import com.nepxion.discovery.plugin.strategy.zuul.event.ZuulStrategyRouteDeletedEvent;
import com.nepxion.discovery.plugin.strategy.zuul.event.ZuulStrategyRouteModifiedEvent;
import com.nepxion.discovery.plugin.strategy.zuul.event.ZuulStrategyRouteUpdatedAllEvent;
import com.nepxion.eventbus.annotation.EventBus;

@EventBus
public class ZuulRouteEventSubscriber {
    @Subscribe
    public void onZuulStrategyRouteAdded(ZuulStrategyRouteAddedEvent zuulStrategyRouteAddedEvent) {
        System.out.println("::::: 推送网关路由变更信息给钉钉，增加网关路由=" + zuulStrategyRouteAddedEvent.getZuulStrategyRouteEntity());
    }

    @Subscribe
    public void onZuulStrategyRouteModified(ZuulStrategyRouteModifiedEvent zuulStrategyRouteModifiedEvent) {
        System.out.println("::::: 推送网关路由变更信息给钉钉，修改网关路由=" + zuulStrategyRouteModifiedEvent.getZuulStrategyRouteEntity());
    }

    @Subscribe
    public void onZuulStrategyRouteDeleted(ZuulStrategyRouteDeletedEvent zuulStrategyRouteDeletedEvent) {
        System.out.println("::::: 推送网关路由变更信息给钉钉，删除网关路由=" + zuulStrategyRouteDeletedEvent.getRouteId());
    }

    @Subscribe
    public void onZuulStrategyRouteUpdatedAll(ZuulStrategyRouteUpdatedAllEvent zuulStrategyRouteUpdatedAllEvent) {
        System.out.println("::::: 推送网关路由变更信息给钉钉，更新全部网关路由=" + zuulStrategyRouteUpdatedAllEvent.getZuulStrategyRouteEntityList());
    }
}