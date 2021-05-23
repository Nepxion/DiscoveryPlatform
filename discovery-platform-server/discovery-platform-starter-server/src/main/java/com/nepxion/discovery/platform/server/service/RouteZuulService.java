package com.nepxion.discovery.platform.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.console.entity.GatewayType;
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;

import java.util.Collection;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public interface RouteZuulService {
    GatewayType GATEWAY_TYPE = GatewayType.ZUUL;

    void publish() throws Exception;

    IPage<RouteZuulDto> page(final String description,
                                final Integer pageNum,
                                final Integer pageSize);

    RouteZuulDto getById(final Long id);

    void insert(final RouteZuulDto routeZuulDto);

    void update(final RouteZuulDto routeZuulDto);

    void enable(final Long id,
                final boolean enabled);

    void logicDelete(final Collection<Long> ids);

    void delete(final Collection<Long> ids);
}