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
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.FormatType;
import com.nepxion.discovery.common.entity.ZuulStrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.mapper.RouteZuulMapper;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import com.nepxion.discovery.platform.server.tool.SequenceTool;

public class RouteZuulServiceImpl extends PlatformPublishAdapter<RouteZuulMapper, RouteZuulDto> implements RouteZuulService {
    @Autowired
    private RouteService routeService;

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Override
    public void publish() throws Exception {
        publish(platformDiscoveryAdapter.getGatewayNames(GATEWAY_TYPE),
                new PublishAction<RouteZuulDto>() {
                    @Override
                    public Object process(RouteZuulDto routeZuulDto) {
                        ZuulStrategyRouteEntity zuulStrategyRouteEntity = new ZuulStrategyRouteEntity();
                        zuulStrategyRouteEntity.setId(routeZuulDto.getRouteId());
                        zuulStrategyRouteEntity.setServiceId(routeZuulDto.getServiceId());
                        zuulStrategyRouteEntity.setPath(routeZuulDto.getPath());
                        zuulStrategyRouteEntity.setUrl(routeZuulDto.getUrl());
                        zuulStrategyRouteEntity.setStripPrefix(routeZuulDto.getStripPrefix());
                        zuulStrategyRouteEntity.setRetryable(routeZuulDto.getRetryable());
                        zuulStrategyRouteEntity.setSensitiveHeaders(new HashSet<>(CommonTool.split(routeZuulDto.getSensitiveHeaders(), ",")));
                        zuulStrategyRouteEntity.setCustomSensitiveHeaders(routeZuulDto.getCustomSensitiveHeaders());
                        return zuulStrategyRouteEntity;
                    }

                    @Override
                    public void publishEmptyConfig(String portalName, List<RouteZuulDto> routeZuulDtoList) throws Exception {
                        updateConfig(portalName, new ArrayList<ZuulStrategyRouteEntity>(0));
                    }

                    @Override
                    public void publishConfig(String portalName, List<Object> configList) throws Exception {
                        updateConfig(portalName, configList);
                    }

                    private void updateConfig(String serviceName, Object config) throws Exception {
                        String groupName = platformDiscoveryAdapter.getGroupName(serviceName);
                        String serviceId = serviceName.concat("-").concat(DiscoveryConstant.DYNAMIC_ROUTE_KEY);
                        platformDiscoveryAdapter.publishConfig(groupName, serviceId, JsonUtil.toPrettyJson(config), FormatType.JSON_FORMAT);
                    }
                });
    }

    @TransactionReader
    @Override
    public IPage<RouteZuulDto> page(String description, Integer pageNum, Integer pageSize) {
        QueryWrapper<RouteZuulDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteZuulDto> lambda = queryWrapper.lambda().orderByAsc(RouteZuulDto::getCreateTime);
        if (StringUtils.isNotEmpty(description)) {
            lambda.like(RouteZuulDto::getDescription, description);
        }
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @TransactionWriter
    @Override
    public void insert(RouteZuulDto routeZuulDto) {
        routeZuulDto = prepareInsert(routeZuulDto);
        if (routeZuulDto == null) {
            return;
        }
        routeZuulDto.setPortalType(BaseStateEntity.PortalType.GATEWAY.getCode());
        Integer nextMaxCreateTimesInDayOfZuul = routeService.getNextMaxCreateTimesInDayOfZuul();
        if (StringUtils.isEmpty(routeZuulDto.getRouteId())) {
            routeZuulDto.setRouteId(SequenceTool.getSequenceId(nextMaxCreateTimesInDayOfZuul));
        }
        routeZuulDto.setCreateTimesInDay(nextMaxCreateTimesInDayOfZuul);
        save(routeZuulDto);
    }
}