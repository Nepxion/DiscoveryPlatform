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
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.entity.po.RouteGatewayPo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlRouteGatewayMapper;
import com.nepxion.discovery.platform.server.service.RouteGatewayService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

public class MySqlRouteGatewayService extends ServiceImpl<MySqlRouteGatewayMapper, RouteGatewayDto> implements RouteGatewayService {
    @Autowired
    private ServiceResource serviceResource;
    @Autowired
    private ConfigResource configResource;

    @TranSave
    @Override
    public void publish() throws Exception {
        final List<RouteGatewayDto> routeGatewayDtoList = this.list();

        if (CollectionUtils.isEmpty(routeGatewayDtoList)) {
            final List<String> gatewayNameList = this.serviceResource.getGatewayList(GATEWAY_TYPE);
            for (final String gatewayName : gatewayNameList) {
                final String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, new ArrayList<RouteGatewayPo>());
            }
            return;
        }

        final List<RouteGatewayDto> toUpdateList = new ArrayList<>(routeGatewayDtoList.size());
        final List<RouteGatewayDto> toDeleteList = new ArrayList<>(routeGatewayDtoList.size());
        final Map<String, List<RouteGatewayDto>> unusedMap = new HashMap<>();

        final Map<String, List<RouteGatewayPo>> newGatewayRouteMap = new HashMap<>();
        for (final RouteGatewayDto routeGatewayDto : routeGatewayDtoList) {
            if (routeGatewayDto.getDeleted()) {
                toDeleteList.add(routeGatewayDto);
                addKV(unusedMap, routeGatewayDto.getGatewayName(), routeGatewayDto);
                continue;
            } else if (!routeGatewayDto.getEnabled()) {
                addKV(unusedMap, routeGatewayDto.getGatewayName(), routeGatewayDto);
                toUpdateList.add(routeGatewayDto);
                continue;
            }

            toUpdateList.add(routeGatewayDto);

            final RouteGatewayPo routeGatewayPo = new RouteGatewayPo();
            routeGatewayPo.setId(routeGatewayDto.getRouteId());
            routeGatewayPo.setUri(routeGatewayDto.getUri());
            routeGatewayPo.setPredicates(Arrays.asList(routeGatewayDto.getPredicates().split(PlatformConstant.ROW_SEPARATOR)));
            routeGatewayPo.setFilters(Arrays.asList(routeGatewayDto.getFilters().split(PlatformConstant.ROW_SEPARATOR)));
            routeGatewayPo.setOrder(routeGatewayDto.getOrder());
            routeGatewayPo.setMetadata(CommonTool.asMap(routeGatewayDto.getMetadata(), PlatformConstant.ROW_SEPARATOR));

            if (newGatewayRouteMap.containsKey(routeGatewayDto.getGatewayName())) {
                newGatewayRouteMap.get(routeGatewayDto.getGatewayName()).add(routeGatewayPo);
            } else {
                final List<RouteGatewayPo> routeGatewayPoList = new ArrayList<>();
                routeGatewayPoList.add(routeGatewayPo);
                newGatewayRouteMap.put(routeGatewayDto.getGatewayName(), routeGatewayPoList);
            }
        }


        if (CollectionUtils.isEmpty(newGatewayRouteMap)) {
            for (Map.Entry<String, List<RouteGatewayDto>> pair : unusedMap.entrySet()) {
                final String gatewayName = pair.getKey();
                final String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, new ArrayList<RouteGatewayPo>());
            }
        } else {
            for (Map.Entry<String, List<RouteGatewayPo>> pair : newGatewayRouteMap.entrySet()) {
                final String gatewayName = pair.getKey();
                final String group = this.serviceResource.getGroup(gatewayName);
                this.updateConfig(gatewayName, group, pair.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(toDeleteList)) {
            this.delete(toDeleteList.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }

        if (!CollectionUtils.isEmpty(toUpdateList)) {
            for (final RouteGatewayDto routeGatewayDto : toUpdateList) {
                routeGatewayDto.setPublish(true);
            }
            this.updateBatchById(toUpdateList, toUpdateList.size());
        }
    }

    @TranRead
    @Override
    public IPage<RouteGatewayDto> page(final String description,
                                       final Integer pageNum,
                                       final Integer pageSize) {
        final QueryWrapper<RouteGatewayDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteGatewayDto> lambda = queryWrapper.lambda();
        if (!ObjectUtils.isEmpty(description)) {
            lambda.eq(RouteGatewayDto::getDescription, description);
        }
        lambda.orderByAsc(RouteGatewayDto::getRowCreateTime);
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @TranRead
    @Override
    public RouteGatewayDto getById(final Long id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

    @TranSave
    @Override
    public void insert(final RouteGatewayDto routeGatewayDto) {
        if (routeGatewayDto == null) {
            return;
        }
        if (ObjectUtils.isEmpty(routeGatewayDto.getRouteId())) {
            routeGatewayDto.setRouteId("gw_".concat(RandomUtil.randomString(15)));
        }
        routeGatewayDto.setOperation(Operation.INSERT.getCode());
        routeGatewayDto.setPublish(false);
        routeGatewayDto.setDeleted(false);
        this.save(routeGatewayDto);
    }

    @TranSave
    @Override
    public void update(final RouteGatewayDto routeGatewayDto) {
        if (routeGatewayDto == null) {
            return;
        }
        routeGatewayDto.setPublish(false);
        routeGatewayDto.setDeleted(false);
        routeGatewayDto.setOperation(Operation.UPDATE.getCode());
        this.updateById(routeGatewayDto);
    }

    @TranSave
    @Override
    public void enable(final Long id,
                       final boolean enabled) {
        final RouteGatewayDto routeGatewayDto = this.getById(id);
        routeGatewayDto.setEnabled(enabled);
        this.update(routeGatewayDto);
    }

    @TranSave
    @Override
    public void logicDelete(final Collection<Long> ids) {
        for (final Long id : ids) {
            final RouteGatewayDto routeGatewayDto = this.getById(id);
            if (routeGatewayDto == null) {
                continue;
            }
            routeGatewayDto.setDeleted(true);
            routeGatewayDto.setPublish(false);
            routeGatewayDto.setOperation(Operation.DELETE.getCode());
            this.updateById(routeGatewayDto);
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

    private void addKV(final Map<String, List<RouteGatewayDto>> map,
                       final String key,
                       final RouteGatewayDto value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<RouteGatewayDto> routeGatewayDtoList = new ArrayList<>();
            routeGatewayDtoList.add(value);
            map.put(key, routeGatewayDtoList);
        }

    }
}