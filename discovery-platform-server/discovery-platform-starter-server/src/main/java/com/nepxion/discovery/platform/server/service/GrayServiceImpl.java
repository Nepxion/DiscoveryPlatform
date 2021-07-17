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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyEntity;
import com.nepxion.discovery.common.entity.StrategyHeaderEntity;
import com.nepxion.discovery.common.entity.StrategyReleaseEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.entity.StrategyRouteType;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import com.nepxion.discovery.platform.server.entity.dto.GrayDto;
import com.nepxion.discovery.platform.server.entity.po.GrayPo;
import com.nepxion.discovery.platform.server.mapper.GrayMapper;

public class GrayServiceImpl extends PlatformPublishAdapter<GrayMapper, GrayDto> implements GrayService {

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

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
                    }

                    @Override
                    public void publishConfig(String portalName, List<Object> configList) throws Exception {
                        for (Object item : configList) {
                            BlueGreenDto blueGreenDto = (BlueGreenDto) item;
                            BlueGreenDto.Type type = BlueGreenDto.Type.get(blueGreenDto.getType());

                            RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                            StrategyReleaseEntity strategyReleaseEntity = new StrategyReleaseEntity();
                            if (ruleEntity.getStrategyReleaseEntity() != null) {
                                strategyReleaseEntity.setStrategyConditionGrayEntityList(ruleEntity.getStrategyReleaseEntity().getStrategyConditionGrayEntityList());
                            }

                            if (hasValue(blueGreenDto.getHeader())) {
                                StrategyHeaderEntity strategyHeaderEntity = new StrategyHeaderEntity();
                                strategyHeaderEntity.setHeaderMap(toMap(blueGreenDto.getHeader()));
                                strategyReleaseEntity.setStrategyHeaderEntity(strategyHeaderEntity);
                            }

                            ConditionAndRoute conditionAndRoute = toConditionAndRoute(blueGreenDto);
                            strategyReleaseEntity.setStrategyConditionBlueGreenEntityList(conditionAndRoute.getConditionBlueGreenEntityList());
                            strategyReleaseEntity.setStrategyRouteEntityList(conditionAndRoute.getStrategyRouteEntityList());

                            if (hasValue(blueGreenDto.getStrategy())) {
                                StrategyConditionBlueGreenEntity strategyConditionBlueGreenEntity = new StrategyConditionBlueGreenEntity();
                                strategyConditionBlueGreenEntity.setId(PlatformConstant.BASIC_CONDITION);

                                StrategyRouteEntity strategyRouteEntity = new StrategyRouteEntity();
                                strategyRouteEntity.setId(PlatformConstant.BASIC_ROUTE);

                                switch (Objects.requireNonNull(type)) {
                                    case VERSION:
                                        strategyConditionBlueGreenEntity.setVersionId(PlatformConstant.BASIC_ROUTE);
                                        strategyRouteEntity.setType(StrategyRouteType.VERSION);
                                        break;
                                    case REGION:
                                        strategyConditionBlueGreenEntity.setRegionId(PlatformConstant.BASIC_ROUTE);
                                        strategyRouteEntity.setType(StrategyRouteType.REGION);
                                        break;
                                }
                                strategyRouteEntity.setValue(toServiceJson(blueGreenDto.getStrategy()));
                                conditionAndRoute.getConditionBlueGreenEntityList().add(strategyConditionBlueGreenEntity);
                                conditionAndRoute.getStrategyRouteEntityList().add(strategyRouteEntity);
                            }

                            ruleEntity.setStrategyReleaseEntity(strategyReleaseEntity);
                            GrayServiceImpl.super.publishConfig(BaseStateEntity.PortalType.get(blueGreenDto.getPortalType()), portalName, ruleEntity);
                        }
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
        grayDto.setStrategy(grayPo.getStrategy());
        grayDto.setCondition(grayPo.getCondition());
        grayDto.setRoute(grayPo.getRoute());
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
        grayDto.setStrategy(grayPo.getStrategy());
        grayDto.setCondition(grayPo.getCondition());
        grayDto.setRoute(grayPo.getRoute());
        grayDto.setRouteService(grayPo.getRouteService());
        grayDto.setHeader(grayPo.getHeader());
        grayDto.setDescription(grayPo.getDescription());
        return updateById(grayDto);
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

    private static ConditionAndRoute toConditionAndRoute(BlueGreenDto blueGreenDto) throws JsonProcessingException {
        ConditionAndRoute conditionAndRoute = new ConditionAndRoute();
        BlueGreenDto.Type type = BlueGreenDto.Type.get(blueGreenDto.getType());

        if (StringUtils.isNotEmpty(blueGreenDto.getCondition())) {
            Map<String, List<Map<String, String>>> conditionMap = JsonUtil.fromJson(blueGreenDto.getCondition(), new TypeReference<Map<String, List<Map<String, String>>>>() {
            });

            List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = new ArrayList<>();

            int index = 0;
            for (Map.Entry<String, List<Map<String, String>>> pair : conditionMap.entrySet()) {
                if (pair.getValue().isEmpty()) {
                    continue;
                }

                StrategyConditionBlueGreenEntity strategyConditionBlueGreenEntity = new StrategyConditionBlueGreenEntity();
                strategyConditionBlueGreenEntity.setId(String.format(PlatformConstant.CONDITION, index));

                switch (Objects.requireNonNull(type)) {
                    case VERSION:
                        strategyConditionBlueGreenEntity.setVersionId(String.format(PlatformConstant.ROUTE, index));
                        break;
                    case REGION:
                        strategyConditionBlueGreenEntity.setRegionId(String.format(PlatformConstant.ROUTE, index));
                        break;
                }
                strategyConditionBlueGreenEntity.setExpression(pair.getValue().get(0).get(PlatformConstant.SPEL_CONDITION));
                strategyConditionBlueGreenEntityList.add(strategyConditionBlueGreenEntity);
                index++;
            }
            conditionAndRoute.setConditionBlueGreenEntityList(strategyConditionBlueGreenEntityList);
        }

        if (StringUtils.isNotEmpty(blueGreenDto.getRoute())) {
            Map<String, List<Map<String, String>>> routeMap = JsonUtil.fromJson(blueGreenDto.getRoute(), new TypeReference<Map<String, List<Map<String, String>>>>() {
            });

            List<StrategyRouteEntity> strategyRouteEntityList = new ArrayList<>();

            int index = 0;
            for (Map.Entry<String, List<Map<String, String>>> entry : routeMap.entrySet()) {
                StrategyRouteEntity strategyRouteEntity = new StrategyRouteEntity();
                switch (Objects.requireNonNull(type)) {
                    case VERSION:
                        strategyRouteEntity.setId(conditionAndRoute.getConditionBlueGreenEntityList().get(index).getVersionId());
                        strategyRouteEntity.setType(StrategyRouteType.VERSION);
                        break;
                    case REGION:
                        strategyRouteEntity.setId(conditionAndRoute.getConditionBlueGreenEntityList().get(index).getRegionId());
                        strategyRouteEntity.setType(StrategyRouteType.REGION);
                        break;
                }
                strategyRouteEntity.setValue(toServiceJson(JsonUtil.toJson(entry.getValue())));
                strategyRouteEntityList.add(strategyRouteEntity);
                index++;
            }
            conditionAndRoute.setStrategyRouteEntityList(strategyRouteEntityList);
        }
        return conditionAndRoute;
    }

    private static class ConditionAndRoute {
        private List<StrategyConditionBlueGreenEntity> conditionBlueGreenEntityList;
        private List<StrategyRouteEntity> strategyRouteEntityList;

        public List<StrategyConditionBlueGreenEntity> getConditionBlueGreenEntityList() {
            return conditionBlueGreenEntityList;
        }

        public void setConditionBlueGreenEntityList(List<StrategyConditionBlueGreenEntity> conditionBlueGreenEntityList) {
            this.conditionBlueGreenEntityList = conditionBlueGreenEntityList;
        }

        public List<StrategyRouteEntity> getStrategyRouteEntityList() {
            return strategyRouteEntityList;
        }

        public void setStrategyRouteEntityList(List<StrategyRouteEntity> strategyRouteEntityList) {
            this.strategyRouteEntityList = strategyRouteEntityList;
        }
    }
}