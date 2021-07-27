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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.mapper.RouteArrangeMapper;

public class RouteArrangeServiceImpl extends PlatformPublishAdapter<RouteArrangeMapper, RouteArrangeDto> implements RouteArrangeService {
    private static final String PREFIX_ROUTE_ID = "route_";
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Override
    public void publish() throws Exception {

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
        routeArrangeDto.setIndex(getNextIndex());
        routeArrangeDto.setRouteId(String.format("%s%s", PREFIX_ROUTE_ID, routeArrangeDto.getIndex()));
        return save(routeArrangeDto);
    }
}