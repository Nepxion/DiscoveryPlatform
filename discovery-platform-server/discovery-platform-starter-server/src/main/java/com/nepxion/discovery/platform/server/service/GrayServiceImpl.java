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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.RegionWeightEntity;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionGrayEntity;
import com.nepxion.discovery.common.entity.StrategyEntity;
import com.nepxion.discovery.common.entity.StrategyHeaderEntity;
import com.nepxion.discovery.common.entity.StrategyReleaseEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.entity.StrategyRouteType;
import com.nepxion.discovery.common.entity.VersionWeightEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.GrayDto;
import com.nepxion.discovery.platform.server.entity.po.GrayPo;
import com.nepxion.discovery.platform.server.mapper.GrayMapper;

public class GrayServiceImpl extends PlatformPublishAdapter<GrayMapper, GrayDto> implements GrayService {
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Lazy
    @Autowired
    private BlueGreenService blueGreenService;

    @Override
    public void publish() throws Exception {
        publish(platformDiscoveryAdapter.getGatewayNames(),
                new PublishAction<GrayDto>() {
                    @Override
                    public Object process(GrayDto grayDto) {
                        return grayDto;
                    }

                    @Override
                    public void publishEmptyConfig(String portalName, List<GrayDto> grayDtoList) throws Exception {
                        RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                        ruleEntity.setStrategyEntity(new StrategyEntity());
                        ruleEntity.setStrategyReleaseEntity(new StrategyReleaseEntity());

                        if (CollectionUtils.isEmpty(grayDtoList)) {
                            platformDiscoveryAdapter.publishConfig(portalName, ruleEntity);
                        } else {
                            Set<BaseStateEntity.PortalType> portalTypeSet = new HashSet<>();
                            for (GrayDto grayDto : grayDtoList) {
                                portalTypeSet.add(BaseStateEntity.PortalType.get(grayDto.getPortalType()));
                            }
                            for (BaseStateEntity.PortalType portalType : portalTypeSet) {
                                GrayServiceImpl.super.publishConfig(portalType, portalName, ruleEntity);
                            }
                        }
                        blueGreenService.updatePublishFlag(portalName, false);
                    }

                    @Override
                    public void publishConfig(String portalName, List<Object> configList) throws Exception {
                        for (Object item : configList) {
                            GrayDto grayDto = (GrayDto) item;
                            GrayDto.Type type = GrayDto.Type.get(grayDto.getType());

                            RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                            StrategyReleaseEntity strategyReleaseEntity = new StrategyReleaseEntity();

                            if (hasValue(grayDto.getHeader())) {
                                StrategyHeaderEntity strategyHeaderEntity = new StrategyHeaderEntity();
                                strategyHeaderEntity.setHeaderMap(toMap(grayDto.getHeader()));
                                strategyReleaseEntity.setStrategyHeaderEntity(strategyHeaderEntity);
                            }

                            ConditionAndRoute conditionAndRoute = toConditionAndRoute(grayDto);
                            strategyReleaseEntity.setStrategyConditionGrayEntityList(conditionAndRoute.getStrategyConditionGrayEntityList());
                            strategyReleaseEntity.setStrategyRouteEntityList(conditionAndRoute.getStrategyRouteEntityList());

                            if (hasValue(grayDto.getBasicStrategy())) {
                                List<Map<String, String>> basicStrategy = JsonUtil.fromJson(grayDto.getBasicStrategy(), new TypeReference<List<Map<String, String>>>() {
                                });

                                Map<String, Integer> weightMap = new LinkedHashMap<>();
                                for (Map<String, String> map : basicStrategy) {
                                    String routeName = conditionAndRoute.getRouteNameKeyMap().get(map.get(PlatformConstant.ROUTE_NAME));
                                    weightMap.put(routeName, Integer.parseInt(map.get(PlatformConstant.VALUE)));
                                }

                                StrategyConditionGrayEntity strategyConditionGrayEntity = new StrategyConditionGrayEntity();
                                strategyConditionGrayEntity.setId(PlatformConstant.BASIC_CONDITION);

                                switch (Objects.requireNonNull(type)) {
                                    case VERSION:
                                        VersionWeightEntity versionWeightEntity = new VersionWeightEntity();
                                        versionWeightEntity.setWeightMap(weightMap);
                                        strategyConditionGrayEntity.setVersionWeightEntity(versionWeightEntity);
                                        break;
                                    case REGION:
                                        RegionWeightEntity regionWeightEntity = new RegionWeightEntity();
                                        regionWeightEntity.setWeightMap(weightMap);
                                        strategyConditionGrayEntity.setRegionWeightEntity(regionWeightEntity);
                                        break;
                                }
                                conditionAndRoute.getStrategyConditionGrayEntityList().add(strategyConditionGrayEntity);
                            }

                            ruleEntity.setStrategyReleaseEntity(strategyReleaseEntity);
                            GrayServiceImpl.super.publishConfig(BaseStateEntity.PortalType.get(grayDto.getPortalType()), portalName, ruleEntity);
                        }
                        blueGreenService.updatePublishFlag(portalName, false);
                    }
                }
        );
    }

    @TransactionReader
    @Override
    public IPage<GrayDto> page(String name, Integer page, Integer limit) {
        LambdaQueryWrapper<GrayDto> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like(GrayDto::getDescription, name);
        }
        queryWrapper.orderByAsc(GrayDto::getCreateTime);
        return this.page(new Page<>(page, limit), queryWrapper);
    }

    @TransactionWriter
    @Override
    public Boolean insert(GrayPo grayPo) {
        GrayDto grayDto = prepareInsert(new GrayDto());
        grayDto.setPortalName(grayPo.getPortalName());
        grayDto.setPortalType(grayPo.getPortalType());
        grayDto.setType(grayPo.getType());
        grayDto.setBasicStrategy(grayPo.getBasicStrategy());
        grayDto.setGrayStrategy(grayPo.getGrayStrategy());
        grayDto.setRouteService(grayPo.getRouteService());
        grayDto.setHeader(grayPo.getHeader());
        grayDto.setDescription(grayPo.getDescription());
        return save(grayDto);
    }

    @TransactionWriter
    @Override
    public Boolean update(GrayPo grayPo) {
        GrayDto grayDto = prepareUpdate(this.getById(grayPo.getId()));
        if (grayDto == null) {
            return false;
        }
        grayDto.setType(grayPo.getType());
        grayDto.setBasicStrategy(grayPo.getBasicStrategy());
        grayDto.setGrayStrategy(grayPo.getGrayStrategy());
        grayDto.setRouteService(grayPo.getRouteService());
        grayDto.setHeader(grayPo.getHeader());
        grayDto.setDescription(grayPo.getDescription());
        return updateById(grayDto);
    }

    @TransactionWriter
    @Override
    public void updatePublishFlag(String portalName, boolean flag) {
        LambdaUpdateWrapper<GrayDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(GrayDto::getPortalName, portalName)
                .set(GrayDto::getPublishFlag, flag);
        update(updateWrapper);
    }

    @TransactionReader
    @Override
    public List<String> listPortalNames() {
        return baseMapper.listPortalNames();
    }

    private static boolean hasValue(String arrayJson) {
        return StringUtils.isNotEmpty(arrayJson) && !DiscoveryConstant.EMPTY_JSON_RULE_MULTIPLE.equals(arrayJson);
    }

    private static Map<String, String> toMap(String arrayJson) {
        Map<String, String> result = new LinkedHashMap<>();

        List<Map<String, String>> list = JsonUtil.fromJson(arrayJson, new TypeReference<List<Map<String, String>>>() {
        });

        for (Map<String, String> map : list) {
            if (map.containsKey(PlatformConstant.SERVICE_NAME)) {
                result.put(map.get(PlatformConstant.SERVICE_NAME), map.get(PlatformConstant.VALUE));
            } else if (map.containsKey(PlatformConstant.HEADER_NAME)) {
                result.put(map.get(PlatformConstant.HEADER_NAME), map.get(PlatformConstant.VALUE));
            }
        }
        return result;
    }

    private static String toServiceJson(String arrayJson) throws JsonProcessingException {
        return JsonUtil.getObjectMapper().writeValueAsString(toMap(arrayJson));
    }

    private static ConditionAndRoute toConditionAndRoute(GrayDto grayDto) throws JsonProcessingException {
        ConditionAndRoute conditionAndRoute = new ConditionAndRoute();
        GrayDto.Type type = GrayDto.Type.get(grayDto.getType());

        if (StringUtils.isNotEmpty(grayDto.getGrayStrategy())) {
            Map<String, ConditionAndRouteJson> conditionRouteMap = JsonUtil.fromJson(grayDto.getGrayStrategy(), new TypeReference<Map<String, ConditionAndRouteJson>>() {
            });
            Map<String, List<Map<String, String>>> routeServiceMap = JsonUtil.fromJson(grayDto.getRouteService(), new TypeReference<Map<String, List<Map<String, String>>>>() {
            });
            Map<String, String> routeNameKeyMap = new HashMap<>();
            conditionAndRoute.setRouteNameKeyMap(routeNameKeyMap);
            int index = 0;
            for (Map.Entry<String, List<Map<String, String>>> entry : routeServiceMap.entrySet()) {
                routeNameKeyMap.put(entry.getKey(), String.format("route-%s", index));
                index++;
            }

            List<StrategyConditionGrayEntity> strategyConditionGrayEntityList = new ArrayList<>();
            List<StrategyRouteEntity> strategyRouteEntityList = new ArrayList<>();
            conditionAndRoute.setStrategyConditionGrayEntityList(strategyConditionGrayEntityList);
            conditionAndRoute.setStrategyRouteEntityList(strategyRouteEntityList);

            index = 0;
            for (Map.Entry<String, ConditionAndRouteJson> pair : conditionRouteMap.entrySet()) {
                List<Map<String, String>> conditionList = pair.getValue().getCondition();
                List<Map<String, String>> routeList = pair.getValue().getRoute();

                StrategyConditionGrayEntity strategyConditionGrayEntity = new StrategyConditionGrayEntity();
                strategyConditionGrayEntity.setId(String.format(PlatformConstant.CONDITION, index));

                Map<String, Integer> weightMap = new LinkedHashMap<>();
                for (Map<String, String> map : routeList) {
                    weightMap.put(routeNameKeyMap.get(map.get(PlatformConstant.ROUTE_NAME)),
                            Integer.parseInt(map.get(PlatformConstant.VALUE)));
                }
                switch (Objects.requireNonNull(type)) {
                    case VERSION:
                        VersionWeightEntity versionWeightEntity = new VersionWeightEntity();
                        versionWeightEntity.setWeightMap(weightMap);
                        strategyConditionGrayEntity.setVersionWeightEntity(versionWeightEntity);
                        break;
                    case REGION:
                        RegionWeightEntity regionWeightEntity = new RegionWeightEntity();
                        regionWeightEntity.setWeightMap(weightMap);
                        strategyConditionGrayEntity.setRegionWeightEntity(regionWeightEntity);
                        break;
                }
                strategyConditionGrayEntity.setExpression(conditionList.get(0).get(PlatformConstant.SPEL_CONDITION));
                strategyConditionGrayEntityList.add(strategyConditionGrayEntity);
                index++;
            }

            for (Map.Entry<String, List<Map<String, String>>> pair : routeServiceMap.entrySet()) {
                StrategyRouteEntity strategyRouteEntity = new StrategyRouteEntity();
                strategyRouteEntity.setId(routeNameKeyMap.get(pair.getKey()));

                switch (Objects.requireNonNull(type)) {
                    case VERSION:
                        strategyRouteEntity.setType(StrategyRouteType.VERSION);
                        break;
                    case REGION:
                        strategyRouteEntity.setType(StrategyRouteType.REGION);
                        break;
                }
                strategyRouteEntity.setValue(toServiceJson(JsonUtil.toJson(pair.getValue())));
                strategyRouteEntityList.add(strategyRouteEntity);
            }
        }
        return conditionAndRoute;
    }

    private static class ConditionAndRoute {
        private List<StrategyConditionGrayEntity> strategyConditionGrayEntityList;
        private List<StrategyRouteEntity> strategyRouteEntityList;
        private Map<String, String> routeNameKeyMap;

        public List<StrategyConditionGrayEntity> getStrategyConditionGrayEntityList() {
            return strategyConditionGrayEntityList;
        }

        public void setStrategyConditionGrayEntityList(List<StrategyConditionGrayEntity> strategyConditionGrayEntityList) {
            this.strategyConditionGrayEntityList = strategyConditionGrayEntityList;
        }

        public List<StrategyRouteEntity> getStrategyRouteEntityList() {
            return strategyRouteEntityList;
        }

        public void setStrategyRouteEntityList(List<StrategyRouteEntity> strategyRouteEntityList) {
            this.strategyRouteEntityList = strategyRouteEntityList;
        }

        public Map<String, String> getRouteNameKeyMap() {
            return routeNameKeyMap;
        }

        public void setRouteNameKeyMap(Map<String, String> routeNameKeyMap) {
            this.routeNameKeyMap = routeNameKeyMap;
        }
    }

    private static class ConditionAndRouteJson {
        private List<Map<String, String>> condition;
        private List<Map<String, String>> route;

        public List<Map<String, String>> getCondition() {
            return condition;
        }

        public void setCondition(List<Map<String, String>> condition) {
            this.condition = condition;
        }

        public List<Map<String, String>> getRoute() {
            return route;
        }

        public void setRoute(List<Map<String, String>> route) {
            this.route = route;
        }
    }
}