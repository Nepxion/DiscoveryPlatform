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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.dto.RouteStrategyDto;
import com.nepxion.discovery.platform.server.mapper.RouteStrategyMapper;

public class RouteStrategyServiceImpl extends ServiceImpl<RouteStrategyMapper, RouteStrategyDto> implements RouteStrategyService {
    @TransactionWriter
    @Override
    public boolean save(String portalName, Integer portalType, List<String> routeIdList) {
        LambdaQueryWrapper<RouteStrategyDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RouteStrategyDto::getPortalName, portalName)
                .eq(RouteStrategyDto::getPortalType, portalType);
        remove(queryWrapper);

        List<String> routeIdListDistinct = routeIdList.stream().distinct().collect(Collectors.toList());

        List<RouteStrategyDto> needInsert = new ArrayList<>(routeIdListDistinct.size());
        for (String routeId : routeIdListDistinct) {
            if (StringUtils.isEmpty(routeId)) {
                continue;
            }
            RouteStrategyDto routeStrategyDto = new RouteStrategyDto();
            routeStrategyDto.setRouteId(routeId);
            routeStrategyDto.setPortalName(portalName);
            routeStrategyDto.setPortalType(portalType);
            needInsert.add(routeStrategyDto);
        }

        if (!CollectionUtils.isEmpty(needInsert)) {
            saveBatch(needInsert);
        }
        return true;
    }

    @TransactionReader
    @Override
    public List<RouteStrategyDto> getByPortalNameAndPortalType(String portalName, Integer portalType) {
        return list(Wrappers
                .lambdaQuery(RouteStrategyDto.class)
                .eq(RouteStrategyDto::getPortalName, portalName)
                .eq(RouteStrategyDto::getPortalType, portalType)
                .orderByAsc(RouteStrategyDto::getRouteId));
    }

    @TransactionReader
    @Override
    public List<RouteStrategyDto> getByRouteId(String routeId) {
        return list(Wrappers.lambdaQuery(RouteStrategyDto.class).eq(RouteStrategyDto::getRouteId, routeId).orderByAsc(RouteStrategyDto::getPortalName));
    }
}