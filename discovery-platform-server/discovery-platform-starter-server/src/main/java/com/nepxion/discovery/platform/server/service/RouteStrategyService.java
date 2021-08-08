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

import java.util.List;

import com.nepxion.discovery.platform.server.entity.dto.RouteStrategyDto;

public interface RouteStrategyService {
    boolean save(String portalName, Integer portalType, List<String> routeIdList);

    List<RouteStrategyDto> getByPortalNameAndPortalType(String portalName, Integer portalType);

    List<RouteStrategyDto> getByRouteId(String routeId);

    void removeByNameAndType(String portalName, Integer portalType);
}