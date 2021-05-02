package com.nepxion.discovery.platform.client.route;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Haojun Ren
 * @version 1.0
 */


import com.nepxion.discovery.plugin.strategy.zuul.entity.ZuulStrategyRouteEntity;
import com.nepxion.discovery.plugin.strategy.zuul.route.AbstractZuulStrategyRoute;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

// 只给数据库存储用
public class PlatformZuulStrategyRoute extends AbstractZuulStrategyRoute {
    public PlatformZuulStrategyRoute(String servletPath, ZuulProperties zuulProperties) {
        super(servletPath, zuulProperties);
    }

    @PostConstruct
    public void initialize() {
        // 从数据库里获取动态路由列表
        List<ZuulStrategyRouteEntity> zuulStrategyRouteEntityList = new ArrayList<>();
        updateAll(zuulStrategyRouteEntityList);
    }
}