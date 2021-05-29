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
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;

public interface RouteGatewayService {
    GatewayType GATEWAY_TYPE = GatewayType.SPRING_CLOUD_GATEWAY;

    void publish() throws Exception;

    IPage<RouteGatewayDto> page(String description, Integer pageNum, Integer pageSize);

    RouteGatewayDto getById(Long id);

    void insert(RouteGatewayDto routeGatewayDto);

    void update(RouteGatewayDto routeGatewayDto);

    void enable(Long id, boolean enabled);

    void logicDelete(Collection<Long> ids);

    void delete(Collection<Long> ids);
}