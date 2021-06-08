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

import com.nepxion.discovery.platform.server.tool.DateTool;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.mapper.RouteMapper;
import com.nepxion.discovery.platform.server.tool.MybatisPlusTool;

import javax.xml.crypto.Data;

public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    @Override
    public Integer getNextMaxCreateTimesInDayOfGateway() {
        String startTime = DateTool.beginOfDay();
        String endTime = DateTool.getEndOfDay();
        return routeMapper.getNextMaxCreateTimesInDay(MybatisPlusTool.getTableName(RouteGatewayDto.class),startTime,endTime);
    }

  	@Override
    public Integer getNextMaxCreateTimesInDayOfZuul() {
		String startTime = DateTool.beginOfDay();
		String endTime = DateTool.getEndOfDay();
        return routeMapper.getNextMaxCreateTimesInDay(MybatisPlusTool.getTableName(RouteZuulDto.class),startTime,endTime);
    }
}