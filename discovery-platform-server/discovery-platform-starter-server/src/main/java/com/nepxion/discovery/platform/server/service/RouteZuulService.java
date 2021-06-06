package com.nepxion.discovery.platform.server.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.Collection;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.console.entity.GatewayType;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.service.base.BasePublishService;

public interface RouteZuulService extends BasePublishService<RouteZuulDto> {
    GatewayType GATEWAY_TYPE = GatewayType.ZUUL;

    void publish() throws Exception;

    IPage<RouteZuulDto> page(String description, Integer pageNum, Integer pageSize);

    void insert(RouteZuulDto routeZuulDto);

    void logicDelete(Collection<Long> ids);

    void delete(Collection<Long> ids);
}