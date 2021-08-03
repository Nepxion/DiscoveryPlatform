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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.mapper.RouteArrangeMapper;

public class RouteArrangeServiceImpl extends PlatformPublishAdapter<RouteArrangeMapper, RouteArrangeDto> implements RouteArrangeService {
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Override
    public void publish() throws Exception {

    }

    @TransactionReader
    @Override
    public RouteArrangeDto getByRouteId(String routeId) {
        if (StringUtils.isEmpty(routeId)) {
            return null;
        }
        LambdaQueryWrapper<RouteArrangeDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RouteArrangeDto::getRouteId, routeId);
        return getOne(queryWrapper);
    }

    @TransactionReader
    @Override
    public List<RouteArrangeDto> list(Integer strategyType) {
        LambdaQueryWrapper<RouteArrangeDto> queryWrapper = new LambdaQueryWrapper<>();
        if (strategyType != null) {
            queryWrapper.eq(RouteArrangeDto::getStrategyType, strategyType);
        }
        queryWrapper.orderByAsc(RouteArrangeDto::getRouteId, RouteArrangeDto::getCreateTime);
        return list(queryWrapper);
    }

    @TransactionReader
    @Override
    public IPage<RouteArrangeDto> page(String description, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<RouteArrangeDto> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<RouteArrangeDto> lambda = queryWrapper.orderByAsc(RouteArrangeDto::getCreateTime);
        if (StringUtils.isNotEmpty(description)) {
            lambda.like(RouteArrangeDto::getDescription, description);
        }
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @TransactionReader
    @Override
    public Long getNextIndex() {
        return baseMapper.getNextIndex();
    }

    @TransactionWriter
    @Override
    public boolean insert(RouteArrangeDto routeArrangeDto) {
        routeArrangeDto = prepareInsert(routeArrangeDto);
        if (StringUtils.isEmpty(routeArrangeDto.getServiceArrange())) {
            routeArrangeDto.setServiceArrange(DiscoveryConstant.EMPTY_JSON_RULE_MULTIPLE);
        }
        routeArrangeDto.setIndex(getNextIndex());
        routeArrangeDto.setRouteId(String.format(PlatformConstant.ROUTE, routeArrangeDto.getIndex()));
        return save(routeArrangeDto);
    }
}