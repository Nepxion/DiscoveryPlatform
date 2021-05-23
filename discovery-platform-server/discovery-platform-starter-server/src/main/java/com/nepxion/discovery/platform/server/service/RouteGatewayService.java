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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.console.entity.GatewayType;
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;

import java.util.Collection;

public interface RouteGatewayService {
    GatewayType GATEWAY_TYPE = GatewayType.SPRING_CLOUD_GATEWAY;

    void publish() throws Exception;

    IPage<RouteGatewayDto> page(final String description,
                                final Integer pageNum,
                                final Integer pageSize);

    RouteGatewayDto getById(final Long id);

    void insert(final RouteGatewayDto routeGatewayDto);

    void update(final RouteGatewayDto routeGatewayDto);

    void enable(final Long id,
                final boolean enabled);

    void logicDelete(final Collection<Long> ids);

    void delete(final Collection<Long> ids);
}