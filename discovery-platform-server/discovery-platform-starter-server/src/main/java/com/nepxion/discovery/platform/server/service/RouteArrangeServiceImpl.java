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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.entity.dto.RouteStrategyDto;
import com.nepxion.discovery.platform.server.entity.dto.StrategyDto;
import com.nepxion.discovery.platform.server.exception.PlatformException;
import com.nepxion.discovery.platform.server.mapper.RouteArrangeMapper;

public class RouteArrangeServiceImpl extends PlatformPublishAdapter<RouteArrangeMapper, RouteArrangeDto> implements RouteArrangeService {
    @Lazy
    @Autowired
    private StrategyService strategyService;

    @Autowired
    private RouteStrategyService routeStrategyService;

    @Override
    public void publish() throws Exception {
        strategyService.publish();
    }

    @TransactionWriter
    @Override
    public boolean update(RouteArrangeDto routeArrangeDto) throws Exception {
        boolean flag = super.update(routeArrangeDto);
        List<StrategyDto> strategyDtoList = strategyService.getUnPublish();
        for (StrategyDto strategyDto : strategyDtoList) {
            strategyDto.setPublishFlag(false);
            strategyService.update(strategyDto);
        }
        return flag;
    }

    @TransactionWriter
    @Override
    public boolean logicDelete(Collection<Long> ids) {
        for (Long id : ids) {
            RouteArrangeDto routeArrangeDto = getById(id);
            List<RouteStrategyDto> routeStrategyDtoList = routeStrategyService.getByRouteId(routeArrangeDto.getRouteId());
            if (!CollectionUtils.isEmpty(routeStrategyDtoList)) {
                RouteStrategyDto routeStrategyDto = routeStrategyDtoList.get(0);
                BaseStateEntity.PortalType portalType = BaseStateEntity.PortalType.get(routeStrategyDto.getPortalType());
                if (portalType != null) {
                    switch (portalType) {
                        case GATEWAY:
                            throw new PlatformException(String.format("链路[%s]正在被网关[%s]使用", routeArrangeDto.getRouteId(), routeStrategyDto.getPortalName()));
                        case SERVICE:
                            throw new PlatformException(String.format("链路[%s]正在被服务[%s]使用", routeArrangeDto.getRouteId(), routeStrategyDto.getPortalName()));
                        case GROUP:
                            throw new PlatformException(String.format("链路[%s]正在被组[%s]使用", routeArrangeDto.getRouteId(), routeStrategyDto.getPortalName()));
                    }
                }
                throw new PlatformException(String.format("链路[%s]正在被[%s]使用", routeArrangeDto.getRouteId(), routeStrategyDto.getPortalName()));
            }
        }
        return super.logicDelete(ids);
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

    @SuppressWarnings("unchecked")
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

    @TransactionWriter
    @Override
    public void updatePublish(boolean publishFlag) {
        update(Wrappers.lambdaUpdate(RouteArrangeDto.class).set(RouteArrangeDto::getPublishFlag, publishFlag));
    }

    @TransactionWriter
    @Override
    public void removeWithLogicDeleteIsTrue() {
        remove(Wrappers.lambdaQuery(RouteArrangeDto.class).eq(RouteArrangeDto::getDeleteFlag, true));
    }
}