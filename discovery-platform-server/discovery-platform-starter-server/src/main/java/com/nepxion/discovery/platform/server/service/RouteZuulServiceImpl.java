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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.entity.ZuulStrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.mapper.RouteZuulMapper;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import com.nepxion.discovery.platform.server.tool.SequenceTool;

public class RouteZuulServiceImpl extends ServiceImpl<RouteZuulMapper, RouteZuulDto> implements RouteZuulService {
    @Autowired
    private RouteService routeService;

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private ConfigResource configResource;

    @TransactionWriter
    @Override
    public void publish() throws Exception {
        List<RouteZuulDto> routeZuulDtoList = list();

        if (CollectionUtils.isEmpty(routeZuulDtoList)) {
            List<String> gatewayNameList = serviceResource.getGatewayList(GATEWAY_TYPE);
            for (String gatewayName : gatewayNameList) {
                String group = serviceResource.getGroup(gatewayName);
                updateConfig(gatewayName, group, new ArrayList<RouteZuulDto>());
            }
            return;
        }

        List<RouteZuulDto> toUpdateList = new ArrayList<>(routeZuulDtoList.size());
        List<RouteZuulDto> toDeleteList = new ArrayList<>(routeZuulDtoList.size());
        Map<String, List<RouteZuulDto>> unusedMap = new HashMap<>();

        Map<String, List<ZuulStrategyRouteEntity>> newGatewayRouteMap = new HashMap<>();
        for (RouteZuulDto routeZuulDto : routeZuulDtoList) {
            if (routeZuulDto.getDeleteFlag()) {
                toDeleteList.add(routeZuulDto);
                addKV(unusedMap, routeZuulDto.getGatewayName(), routeZuulDto);
                continue;
            } else if (!routeZuulDto.getEnableFlag()) {
                addKV(unusedMap, routeZuulDto.getGatewayName(), routeZuulDto);
                toUpdateList.add(routeZuulDto);
                continue;
            }

            toUpdateList.add(routeZuulDto);

            ZuulStrategyRouteEntity zuulStrategyRouteEntity = new ZuulStrategyRouteEntity();
            zuulStrategyRouteEntity.setId(routeZuulDto.getRouteId());
            zuulStrategyRouteEntity.setServiceId(routeZuulDto.getServiceId());
            zuulStrategyRouteEntity.setPath(routeZuulDto.getPath());
            zuulStrategyRouteEntity.setUrl(routeZuulDto.getUrl());
            zuulStrategyRouteEntity.setStripPrefix(routeZuulDto.getStripPrefix());
            zuulStrategyRouteEntity.setRetryable(routeZuulDto.getRetryable());
            zuulStrategyRouteEntity.setSensitiveHeaders(new HashSet<>(CommonTool.split(routeZuulDto.getSensitiveHeaders(), ",")));
            zuulStrategyRouteEntity.setCustomSensitiveHeaders(routeZuulDto.getCustomSensitiveHeaders());

            if (newGatewayRouteMap.containsKey(routeZuulDto.getGatewayName())) {
                newGatewayRouteMap.get(routeZuulDto.getGatewayName()).add(zuulStrategyRouteEntity);
            } else {
                List<ZuulStrategyRouteEntity> zuulStrategyRouteEntityList = new ArrayList<>();
                zuulStrategyRouteEntityList.add(zuulStrategyRouteEntity);
                newGatewayRouteMap.put(routeZuulDto.getGatewayName(), zuulStrategyRouteEntityList);
            }
        }

        if (CollectionUtils.isEmpty(newGatewayRouteMap)) {
            for (Map.Entry<String, List<RouteZuulDto>> pair : unusedMap.entrySet()) {
                String gatewayName = pair.getKey();
                String group = serviceResource.getGroup(gatewayName);
                updateConfig(gatewayName, group, new ArrayList<ZuulStrategyRouteEntity>());
            }
        } else {
            for (Map.Entry<String, List<ZuulStrategyRouteEntity>> pair : newGatewayRouteMap.entrySet()) {
                String gatewayName = pair.getKey();
                String group = serviceResource.getGroup(gatewayName);
                updateConfig(gatewayName, group, pair.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(toDeleteList)) {
            delete(toDeleteList.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }

        if (!CollectionUtils.isEmpty(toUpdateList)) {
            for (RouteZuulDto routeGatewayDto : toUpdateList) {
                routeGatewayDto.setPublishFlag(true);
            }
            updateBatchById(toUpdateList, toUpdateList.size());
        }
    }

    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public IPage<RouteZuulDto> page(String description, Integer pageNum, Integer pageSize) {
        QueryWrapper<RouteZuulDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteZuulDto> lambda = queryWrapper.lambda().orderByAsc(RouteZuulDto::getCreateTime);
        if (StringUtils.isNotEmpty(description)) {
            lambda.eq(RouteZuulDto::getDescription, description);
        }
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @TransactionReader
    @Override
    public RouteZuulDto getById(Long id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

    @TransactionWriter
    @Override
    public void insert(RouteZuulDto routeZuulDto) {
        if (routeZuulDto == null) {
            return;
        }

        Integer nextMaxCreateTimesInDayOfZuul = routeService.getNextMaxCreateTimesInDayOfZuul();
        if (StringUtils.isEmpty(routeZuulDto.getRouteId())) {
            routeZuulDto.setRouteId(SequenceTool.getSequenceId(nextMaxCreateTimesInDayOfZuul));
        }
        routeZuulDto.setCreateTimesInDay(nextMaxCreateTimesInDayOfZuul);
        routeZuulDto.setOperation(Operation.INSERT.getCode());
        routeZuulDto.setPublishFlag(false);
        routeZuulDto.setDeleteFlag(false);
        save(routeZuulDto);
    }

    @TransactionWriter
    @Override
    public void update(RouteZuulDto routeZuulDto) {
        if (routeZuulDto == null) {
            return;
        }
        routeZuulDto.setPublishFlag(false);
        routeZuulDto.setDeleteFlag(false);
        routeZuulDto.setOperation(Operation.UPDATE.getCode());
        updateById(routeZuulDto);
    }

    @TransactionWriter
    @Override
    public void enable(Long id, boolean enableFlag) {
        RouteZuulDto routeZuulDto = getById(id);
        routeZuulDto.setEnableFlag(enableFlag);
        update(routeZuulDto);
    }

    @TransactionWriter
    @Override
    public void logicDelete(Collection<Long> ids) {
        for (Long id : ids) {
            RouteZuulDto routeZuulDto = getById(id);
            if (routeZuulDto == null) {
                continue;
            }
            routeZuulDto.setDeleteFlag(true);
            routeZuulDto.setPublishFlag(false);
            routeZuulDto.setOperation(Operation.DELETE.getCode());
            updateById(routeZuulDto);
        }
    }

    @TransactionWriter
    @Override
    public void delete(Collection<Long> ids) {
        removeByIds(ids);
    }

    private void updateConfig(String gatewayName, String group, Object config) throws Exception {
        String serviceId = gatewayName.concat("-").concat(PlatformConstant.GATEWAY_DYNAMIC_ROUTE);
        configResource.updateRemoteConfig(group, serviceId, JsonUtil.toPrettyJson(config));
    }

    private void addKV(Map<String, List<RouteZuulDto>> map, String key, RouteZuulDto value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<RouteZuulDto> routeGatewayDtoList = new ArrayList<>();
            routeGatewayDtoList.add(value);
            map.put(key, routeGatewayDtoList);
        }
    }
}