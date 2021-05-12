package com.nepxion.discovery.platform.server.ui.interfaces;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.ui.entity.dto.RouteGateway;

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

    IPage<RouteGateway> page(final String description,
                             final Integer pageNum,
                             final Integer pageSize);

    RouteGateway getById(final Long id);

    void insert(final RouteGateway routeGateway);

    void update(final RouteGateway routeGateway);

    void delete(final Collection<Long> ids);

    void enable(final Long id,
                final boolean enabled);

    List<RouteGateway> listEnabled();
}
