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

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.annotation.TranRead;
import com.nepxion.discovery.platform.server.annotation.TranSave;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.entity.po.RouteZuulPo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlRouteZuulMapper;
import com.nepxion.discovery.platform.server.service.RouteZuulService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

public class MySqlRouteZuulService extends ServiceImpl<MySqlRouteZuulMapper, RouteZuulDto> implements RouteZuulService {
    @Autowired
    private ServiceResource serviceResource;
    @Autowired
    private ConfigResource configResource;

    @TranSave
    @Override
    public void publish() throws Exception {
        final List<RouteZuulDto> routeZuulDtoList = this.list();

        if (CollectionUtils.isEmpty(routeZuulDtoList)) {
            final List<String> gatewayNameList = this.serviceResource.getGatewayList(GATEWAY_TYPE);
            for (final String gatewayName : gatewayNameList) {
                final String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, new ArrayList<RouteZuulDto>());
            }
            return;
        }

        final List<RouteZuulDto> toUpdateList = new ArrayList<>(routeZuulDtoList.size());
        final List<RouteZuulDto> toDeleteList = new ArrayList<>(routeZuulDtoList.size());
        final Map<String, List<RouteZuulDto>> unusedMap = new HashMap<>();

        final Map<String, List<RouteZuulPo>> newGatewayRouteMap = new HashMap<>();
        for (final RouteZuulDto routeZuulDto : routeZuulDtoList) {
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

            final RouteZuulPo routeZuulPo = new RouteZuulPo();
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
                final List<RouteZuulPo> routeGatewayPoList = new ArrayList<>();
                routeGatewayPoList.add(routeZuulPo);
                newGatewayRouteMap.put(routeZuulDto.getGatewayName(), routeGatewayPoList);
            }
        }

        if (CollectionUtils.isEmpty(newGatewayRouteMap)) {
            for (Map.Entry<String, List<RouteZuulDto>> pair : unusedMap.entrySet()) {
                final String gatewayName = pair.getKey();
                final String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, new ArrayList<RouteZuulPo>());
            }
        } else {
            for (Map.Entry<String, List<RouteZuulPo>> pair : newGatewayRouteMap.entrySet()) {
                final String gatewayName = pair.getKey();
                final String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, pair.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(toDeleteList)) {
            this.delete(toDeleteList.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }

        if (!CollectionUtils.isEmpty(toUpdateList)) {
            for (final RouteZuulDto routeGatewayDto : toUpdateList) {
                routeGatewayDto.setPublish(true);
            }
            this.updateBatchById(toUpdateList, toUpdateList.size());
        }
    }

    @TranRead
    @Override
    public IPage<RouteZuulDto> page(final String description,
                                    final Integer pageNum,
                                    final Integer pageSize) {
        final QueryWrapper<RouteZuulDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteZuulDto> lambda = queryWrapper.lambda();
        if (!ObjectUtils.isEmpty(description)) {
            lambda.eq(RouteZuulDto::getDescription, description);
        }
        lambda.orderByAsc(RouteZuulDto::getRowCreateTime);
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @TranRead
    @Override
    public RouteZuulDto getById(final Long id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

    @TranSave
    @Override
    public void insert(final RouteZuulDto routeZuulDto) {
        if (routeZuulDto == null) {
            return;
        }
        if (ObjectUtils.isEmpty(routeZuulDto.getRouteId())) {
            routeZuulDto.setRouteId("zl_".concat(RandomUtil.randomString(15)));
        }
        routeZuulDto.setOperation(Operation.INSERT.getCode());
        routeZuulDto.setPublish(false);
        routeZuulDto.setDeleted(false);
        this.save(routeZuulDto);
    }

    @TranSave
    @Override
    public void update(final RouteZuulDto routeZuulDto) {
        if (routeZuulDto == null) {
            return;
        }
        routeZuulDto.setPublish(false);
        routeZuulDto.setDeleted(false);
        routeZuulDto.setOperation(Operation.UPDATE.getCode());
        this.updateById(routeZuulDto);
    }

    @TranSave
    @Override
    public void enable(final Long id,
                       final boolean enabled) {
        final RouteZuulDto routeZuulDto = this.getById(id);
        routeZuulDto.setEnabled(enabled);
        this.update(routeZuulDto);
    }

    @TranSave
    @Override
    public void logicDelete(final Collection<Long> ids) {
        for (final Long id : ids) {
            final RouteZuulDto routeZuulDto = this.getById(id);
            if (routeZuulDto == null) {
                continue;
            }
            routeZuulDto.setDeleted(true);
            routeZuulDto.setPublish(false);
            routeZuulDto.setOperation(Operation.DELETE.getCode());
            this.updateById(routeZuulDto);
        }
    }

    @TranSave
    @Override
    public void delete(final Collection<Long> ids) {
        this.removeByIds(ids);
    }

    private void updateConfig(String gatewayName,
                              String group,
                              Object config) throws Exception {
        final String serviceId = gatewayName.concat("-").concat(PlatformConstant.GATEWAY_DYNAMIC_ROUTE);
        this.configResource.updateRemoteConfig(group, serviceId, JsonUtil.toPrettyJson(config));
    }

    private void addKV(final Map<String, List<RouteZuulDto>> map,
                       final String key,
                       final RouteZuulDto value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<RouteZuulDto> routeGatewayDtoList = new ArrayList<>();
            routeGatewayDtoList.add(value);
            map.put(key, routeGatewayDtoList);
        }

    }
}