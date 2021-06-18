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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.FormatType;
import com.nepxion.discovery.common.entity.GatewayStrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.mapper.RouteGatewayMapper;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import com.nepxion.discovery.platform.server.tool.SequenceTool;

public class RouteGatewayServiceImpl extends PlatformPublishAdapter<RouteGatewayMapper, RouteGatewayDto> implements RouteGatewayService {
    @Autowired
    private RouteService routeService;

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Override
    public void publish() throws Exception {
        publish(platformDiscoveryAdapter.getGatewayNames(GATEWAY_TYPE),
                new PublishAction<RouteGatewayDto>() {
                    @Override
                    public Object process(RouteGatewayDto routeGatewayDto) throws Exception {
                        GatewayStrategyRouteEntity gatewayStrategyRouteEntity = new GatewayStrategyRouteEntity();
                        gatewayStrategyRouteEntity.setId(routeGatewayDto.getRouteId());
                        gatewayStrategyRouteEntity.setUri(routeGatewayDto.getUri());

                        if (StringUtils.isNotEmpty(routeGatewayDto.getPredicates())) {
                            gatewayStrategyRouteEntity.setPredicates(Arrays.asList(routeGatewayDto.getPredicates().split(PlatformConstant.ROW_SEPARATOR)));
                        }
                        if (StringUtils.isNotEmpty(routeGatewayDto.getUserPredicates())) {
                            List<GatewayStrategyRouteEntity.Predicate> predicateList = parse(routeGatewayDto.getUserPredicates(), GatewayStrategyRouteEntity.Predicate.class);
                            gatewayStrategyRouteEntity.setUserPredicates(predicateList);
                        }
                        if (StringUtils.isNotEmpty(routeGatewayDto.getFilters())) {
                            gatewayStrategyRouteEntity.setFilters(Arrays.asList(routeGatewayDto.getFilters().split(PlatformConstant.ROW_SEPARATOR)));
                        }
                        if (StringUtils.isNotEmpty(routeGatewayDto.getUserFilters())) {
                            List<GatewayStrategyRouteEntity.Filter> filterList = parse(routeGatewayDto.getUserFilters(), GatewayStrategyRouteEntity.Filter.class);
                            gatewayStrategyRouteEntity.setUserFilters(filterList);
                        }
                        gatewayStrategyRouteEntity.setOrder(routeGatewayDto.getOrder());
                        gatewayStrategyRouteEntity.setMetadata(CommonTool.asMap(routeGatewayDto.getMetadata(), PlatformConstant.ROW_SEPARATOR));
                        return gatewayStrategyRouteEntity;
                    }

                    @Override
                    public void publishEmptyConfig(String gatewayName) throws Exception {
                        updateConfig(gatewayName, new ArrayList<GatewayStrategyRouteEntity>(0));
                    }

                    @Override
                    public void publishConfig(String gatewayName, List<Object> configList) throws Exception {
                        updateConfig(gatewayName, configList);
                    }

                    private void updateConfig(String serviceName, Object config) throws Exception {
                        String groupName = platformDiscoveryAdapter.getGroupName(serviceName);
                        String serviceId = serviceName.concat("-").concat(DiscoveryConstant.DYNAMIC_ROUTE_KEY);
                        platformDiscoveryAdapter.publishConfig(groupName, serviceId, JsonUtil.toPrettyJson(config), FormatType.XML_FORMAT);
                    }
                });
    }

    @TransactionReader
    @Override
    public IPage<RouteGatewayDto> page(String description, Integer pageNum, Integer pageSize) {
        QueryWrapper<RouteGatewayDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteGatewayDto> lambda = queryWrapper.lambda().orderByAsc(RouteGatewayDto::getCreateTime);
        if (StringUtils.isNotEmpty(description)) {
            lambda.like(RouteGatewayDto::getDescription, description);
        }
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @TransactionWriter
    @Override
    public void insert(RouteGatewayDto routeGatewayDto) {
        routeGatewayDto = prepareInsert(routeGatewayDto);
        if (routeGatewayDto == null) {
            return;
        }
        Integer nextMaxCreateTimesInDayOfGateway = routeService.getNextMaxCreateTimesInDayOfGateway();
        if (StringUtils.isEmpty(routeGatewayDto.getRouteId())) {
            routeGatewayDto.setRouteId(SequenceTool.getSequenceId(nextMaxCreateTimesInDayOfGateway));
        }
        routeGatewayDto.setCreateTimesInDay(nextMaxCreateTimesInDayOfGateway);
        save(routeGatewayDto);
    }

    @SuppressWarnings("unchecked")
    private <T extends GatewayStrategyRouteEntity.Clause> List<T> parse(String value, Class<T> tClass) throws Exception {
        List<T> result = new ArrayList<>();
        String[] all = value.split(PlatformConstant.ROW_SEPARATOR);
        for (String item : all) {
            T t = tClass.newInstance();
            int index = item.indexOf("=");
            if (index < 0) {
                continue;
            }
            String name = item.substring(0, index).trim();
            String json = item.substring(index + 1).trim();
            Map<String, String> map = JsonUtil.fromJson(json, Map.class);
            t.setName(name);
            t.setArgs(map);
            result.add(t);
        }
        return result;
    }
}