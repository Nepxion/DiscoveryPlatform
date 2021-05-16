package com.nepxion.discovery.platform.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;

import java.util.Collection;
import java.util.List;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public interface RouteGatewayService {

    IPage<RouteGatewayDto> page(final String description,
                                final Integer pageNum,
                                final Integer pageSize);

    RouteGatewayDto getById(final Long id);

    void insert(final RouteGatewayDto routeGateway);

    void update(final RouteGatewayDto routeGateway);

    void delete(final Collection<Long> ids);

    void enable(final Long id,
                final boolean enabled);

    List<RouteGatewayDto> listEnabled();
}
