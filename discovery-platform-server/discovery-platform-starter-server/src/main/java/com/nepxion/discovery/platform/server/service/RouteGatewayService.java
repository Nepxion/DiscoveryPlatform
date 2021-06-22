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
import com.nepxion.discovery.platform.server.service.base.BasePublishService;

public interface RouteGatewayService extends BasePublishService<RouteGatewayDto> {
    GatewayType GATEWAY_TYPE = GatewayType.SPRING_CLOUD_GATEWAY;

    void publish() throws Exception;

    IPage<RouteGatewayDto> page(String description, Integer pageNum, Integer pageSize);

    void insert(RouteGatewayDto routeGatewayDto);

    void logicDelete(Collection<Long> ids);

    void delete(Collection<Long> ids);
}