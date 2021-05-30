package com.nepxion.discovery.platform.server.service.impl;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.mapper.RouteMapper;
import com.nepxion.discovery.platform.server.service.RouteService;
import com.nepxion.discovery.platform.server.tool.MybatisPlusTool;

public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    public Integer getNextMaxCreateTimesInDayOfGateway() {
        return routeMapper.getNextMaxCreateTimesInDay(MybatisPlusTool.getTableName(RouteGatewayDto.class));
    }

    public Integer getNextMaxCreateTimesInDayOfZuul() {
        return routeMapper.getNextMaxCreateTimesInDay(MybatisPlusTool.getTableName(RouteZuulDto.class));
    }
}