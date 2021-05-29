package com.nepxion.discovery.platform.server.mysql.service;

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
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.entity.po.RouteZuulPo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlRouteZuulMapper;
import com.nepxion.discovery.platform.server.service.RouteZuulService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import com.nepxion.discovery.platform.server.tool.DateTool;

public class MySqlRouteZuulService extends ServiceImpl<MySqlRouteZuulMapper, RouteZuulDto> implements RouteZuulService {
    @Autowired
    private ServiceResource serviceResource;
    @Autowired
    private ConfigResource configResource;

    @TransactionWriter
    @Override
    public void publish() throws Exception {
        List<RouteZuulDto> routeZuulDtoList = this.list();

        if (CollectionUtils.isEmpty(routeZuulDtoList)) {
            List<String> gatewayNameList = this.serviceResource.getGatewayList(GATEWAY_TYPE);
            for (String gatewayName : gatewayNameList) {
                String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, new ArrayList<RouteZuulDto>());
            }
            return;
        }

        List<RouteZuulDto> toUpdateList = new ArrayList<>(routeZuulDtoList.size());
        List<RouteZuulDto> toDeleteList = new ArrayList<>(routeZuulDtoList.size());
        Map<String, List<RouteZuulDto>> unusedMap = new HashMap<>();

        Map<String, List<RouteZuulPo>> newGatewayRouteMap = new HashMap<>();
        for (RouteZuulDto routeZuulDto : routeZuulDtoList) {
            if (routeZuulDto.getDeleted()) {
                toDeleteList.add(routeZuulDto);
                addKV(unusedMap, routeZuulDto.getGatewayName(), routeZuulDto);
                continue;
            } else if (!routeZuulDto.getEnabled()) {
                addKV(unusedMap, routeZuulDto.getGatewayName(), routeZuulDto);
                toUpdateList.add(routeZuulDto);
                continue;
            }

            toUpdateList.add(routeZuulDto);

            RouteZuulPo routeZuulPo = new RouteZuulPo();
            routeZuulPo.setId(routeZuulDto.getRouteId());
            routeZuulPo.setServiceId(routeZuulDto.getServiceId());
            routeZuulPo.setPath(routeZuulDto.getPath());
            routeZuulPo.setUrl(routeZuulDto.getUrl());
            routeZuulPo.setStripPrefix(routeZuulDto.getStripPrefix());
            routeZuulPo.setRetryable(routeZuulDto.getRetryable());
            routeZuulPo.setSensitiveHeaders(new HashSet<>(CommonTool.split(routeZuulDto.getSensitiveHeaders(), ",")));
            routeZuulPo.setCustomSensitiveHeaders(routeZuulDto.getCustomSensitiveHeaders());

            if (newGatewayRouteMap.containsKey(routeZuulDto.getGatewayName())) {
                newGatewayRouteMap.get(routeZuulDto.getGatewayName()).add(routeZuulPo);
            } else {
                List<RouteZuulPo> routeGatewayPoList = new ArrayList<>();
                routeGatewayPoList.add(routeZuulPo);
                newGatewayRouteMap.put(routeZuulDto.getGatewayName(), routeGatewayPoList);
            }
        }

        if (CollectionUtils.isEmpty(newGatewayRouteMap)) {
            for (Map.Entry<String, List<RouteZuulDto>> pair : unusedMap.entrySet()) {
                String gatewayName = pair.getKey();
                String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, new ArrayList<RouteZuulPo>());
            }
        } else {
            for (Map.Entry<String, List<RouteZuulPo>> pair : newGatewayRouteMap.entrySet()) {
                String gatewayName = pair.getKey();
                String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, pair.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(toDeleteList)) {
            this.delete(toDeleteList.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }

        if (!CollectionUtils.isEmpty(toUpdateList)) {
            for (RouteZuulDto routeGatewayDto : toUpdateList) {
                routeGatewayDto.setPublish(true);
            }
            this.updateBatchById(toUpdateList, toUpdateList.size());
        }
    }

    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public IPage<RouteZuulDto> page(String description, Integer pageNum, Integer pageSize) {
        QueryWrapper<RouteZuulDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteZuulDto> lambda = queryWrapper.lambda().orderByAsc(RouteZuulDto::getRowCreateTime);
        if (StringUtils.isNotEmpty(description)) {
            lambda.eq(RouteZuulDto::getDescription, description);
        }
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
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
        if (StringUtils.isEmpty(routeZuulDto.getRouteId())) {
            routeZuulDto.setRouteId(String.format("zl_%s_%s", DateTool.getSequence(), UUID.randomUUID()));
        }
        routeZuulDto.setOperation(Operation.INSERT.getCode());
        routeZuulDto.setPublish(false);
        routeZuulDto.setDeleted(false);
        this.save(routeZuulDto);
    }

    @TransactionWriter
    @Override
    public void update(RouteZuulDto routeZuulDto) {
        if (routeZuulDto == null) {
            return;
        }
        routeZuulDto.setPublish(false);
        routeZuulDto.setDeleted(false);
        routeZuulDto.setOperation(Operation.UPDATE.getCode());
        this.updateById(routeZuulDto);
    }

    @TransactionWriter
    @Override
    public void enable(Long id,
                       boolean enabled) {
        RouteZuulDto routeZuulDto = this.getById(id);
        routeZuulDto.setEnabled(enabled);
        this.update(routeZuulDto);
    }

    @TransactionWriter
    @Override
    public void logicDelete(Collection<Long> ids) {
        for (Long id : ids) {
            RouteZuulDto routeZuulDto = this.getById(id);
            if (routeZuulDto == null) {
                continue;
            }
            routeZuulDto.setDeleted(true);
            routeZuulDto.setPublish(false);
            routeZuulDto.setOperation(Operation.DELETE.getCode());
            this.updateById(routeZuulDto);
        }
    }

    @TransactionWriter
    @Override
    public void delete(Collection<Long> ids) {
        this.removeByIds(ids);
    }

    private void updateConfig(String gatewayName, String group, Object config) throws Exception {
        String serviceId = gatewayName.concat("-").concat(PlatformConstant.GATEWAY_DYNAMIC_ROUTE);
        this.configResource.updateRemoteConfig(group, serviceId, JsonUtil.toPrettyJson(config));
    }

    private void addKV(Map<String, List<RouteZuulDto>> map,
                       String key,
                       RouteZuulDto value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<RouteZuulDto> routeGatewayDtoList = new ArrayList<>();
            routeGatewayDtoList.add(value);
            map.put(key, routeGatewayDtoList);
        }
    }
}