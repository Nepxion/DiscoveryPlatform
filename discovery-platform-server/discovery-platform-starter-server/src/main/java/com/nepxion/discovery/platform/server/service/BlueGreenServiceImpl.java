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
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.nepxion.discovery.platform.server.entity.dto.GraphDto;
import com.nepxion.discovery.platform.server.entity.po.BlueGreenPo;
import com.nepxion.discovery.platform.server.mapper.BlueGreenMapper;
import com.nepxion.discovery.platform.server.tool.BlueGreenAndGrayGraphTool;

public class BlueGreenServiceImpl extends PlatformPublishAdapter<BlueGreenMapper, BlueGreenDto> implements BlueGreenService {
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Lazy
    @Autowired
    private GrayService grayService;

    @Override
    public void publish() throws Exception {
        publish(platformDiscoveryAdapter.getGatewayNames(),
                new PublishAction<BlueGreenDto>() {
                    @Override
                    public Object process(BlueGreenDto blueGreenDto) {
                        return blueGreenDto;
                    }

                    @Override
                    public void publishEmptyConfig(String portalName, List<BlueGreenDto> blueGreenDtoList) throws Exception {
                        RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                        ruleEntity.setStrategyEntity(new StrategyEntity());
                        ruleEntity.setStrategyReleaseEntity(new StrategyReleaseEntity());

                        if (CollectionUtils.isEmpty(blueGreenDtoList)) {
                            platformDiscoveryAdapter.publishConfig(portalName, ruleEntity);
                        } else {
                            Set<BaseStateEntity.PortalType> portalTypeSet = new HashSet<>();
                            for (BlueGreenDto blueGreenDto : blueGreenDtoList) {
                                portalTypeSet.add(BaseStateEntity.PortalType.get(blueGreenDto.getPortalType()));
                            }
                            for (BaseStateEntity.PortalType portalType : portalTypeSet) {
                                BlueGreenServiceImpl.super.publishConfig(portalType, portalName, ruleEntity);
                            }
                        }
                        grayService.updatePublishFlag(portalName, false);
                    }

                    @Override
                    public void publishConfig(String portalName, List<Object> configList) throws Exception {
                        for (Object item : configList) {
                            BlueGreenDto blueGreenDto = (BlueGreenDto) item;
                            RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                            StrategyReleaseEntity strategyReleaseEntity = toStrategyReleaseEntity(blueGreenDto);
                            ruleEntity.setStrategyReleaseEntity(strategyReleaseEntity);
                            BlueGreenServiceImpl.super.publishConfig(BaseStateEntity.PortalType.get(blueGreenDto.getPortalType()), portalName, ruleEntity);
                        }
                        grayService.updatePublishFlag(portalName, false);
                    }
                }
        );
    }

    private static StrategyReleaseEntity toStrategyReleaseEntity(BlueGreenDto blueGreenDto) throws JsonProcessingException {
        BlueGreenDto.Type type = BlueGreenDto.Type.get(blueGreenDto.getType());
        StrategyReleaseEntity strategyReleaseEntity = new StrategyReleaseEntity();
        if (hasValue(blueGreenDto.getHeader())) {
            StrategyHeaderEntity strategyHeaderEntity = new StrategyHeaderEntity();
            strategyHeaderEntity.setHeaderMap(toMap(blueGreenDto.getHeader()));
            strategyReleaseEntity.setStrategyHeaderEntity(strategyHeaderEntity);
        }

        ConditionAndRoute conditionAndRoute = toConditionAndRoute(blueGreenDto);
        strategyReleaseEntity.setStrategyConditionBlueGreenEntityList(conditionAndRoute.getConditionBlueGreenEntityList());
        strategyReleaseEntity.setStrategyRouteEntityList(conditionAndRoute.getStrategyRouteEntityList());

        if (hasValue(blueGreenDto.getBasicStrategy())) {
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
            strategyRouteEntity.setValue(toServiceJson(blueGreenDto.getBasicStrategy()));
            conditionAndRoute.getConditionBlueGreenEntityList().add(strategyConditionBlueGreenEntity);
            conditionAndRoute.getStrategyRouteEntityList().add(strategyRouteEntity);
        }

        return strategyReleaseEntity;
    }

    @TransactionReader
    @Override
    public IPage<BlueGreenDto> page(String name, Integer page, Integer limit) {
        LambdaQueryWrapper<BlueGreenDto> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like(BlueGreenDto::getDescription, name);
        }
        queryWrapper.orderByAsc(BlueGreenDto::getCreateTime);
        return this.page(new Page<>(page, limit), queryWrapper);
    }

    @TransactionWriter
    @Override
    public Boolean insert(BlueGreenPo blueGreenPo) {
        BlueGreenDto blueGreenDto = prepareInsert(new BlueGreenDto());
        blueGreenDto.setPortalName(blueGreenPo.getPortalName());
        blueGreenDto.setPortalType(blueGreenPo.getPortalType());
        blueGreenDto.setType(blueGreenPo.getType());
        blueGreenDto.setBasicStrategy(blueGreenPo.getBasicStrategy());
        blueGreenDto.setBlueGreenStrategy(blueGreenPo.getBlueGreenStrategy());
        blueGreenDto.setHeader(blueGreenPo.getHeader());
        blueGreenDto.setDescription(blueGreenPo.getDescription());
        return save(blueGreenDto);
    }

    @TransactionWriter
    @Override
    public Boolean update(BlueGreenPo blueGreenPo) {
        BlueGreenDto blueGreenDto = prepareUpdate(this.getById(blueGreenPo.getId()));
        if (blueGreenDto == null) {
            return false;
        }
        blueGreenDto.setType(blueGreenPo.getType());
        blueGreenDto.setBasicStrategy(blueGreenPo.getBasicStrategy());
        blueGreenDto.setBlueGreenStrategy(blueGreenPo.getBlueGreenStrategy());
        blueGreenDto.setHeader(blueGreenPo.getHeader());
        blueGreenDto.setDescription(blueGreenPo.getDescription());
        return updateById(blueGreenDto);
    }

    @Override
    public GraphDto viewGraph(Long id) throws Exception {
        BlueGreenDto blueGreenDto = getById(id);
        if (blueGreenDto == null) {
            return new GraphDto();
        }

        return BlueGreenAndGrayGraphTool.convertRuleEntityToGraph(blueGreenDto.getPortalName(),
                toStrategyReleaseEntity(blueGreenDto));
    }

    @TransactionWriter
    @Override
    public void updatePublishFlag(String portalName, boolean flag) {
        LambdaUpdateWrapper<BlueGreenDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(BlueGreenDto::getPortalName, portalName)
                .set(BlueGreenDto::getPublishFlag, flag);
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

    private static ConditionAndRoute toConditionAndRoute(BlueGreenDto blueGreenDto) throws JsonProcessingException {
        ConditionAndRoute conditionAndRoute = new ConditionAndRoute();
        BlueGreenDto.Type type = BlueGreenDto.Type.get(blueGreenDto.getType());

        if (StringUtils.isNotEmpty(blueGreenDto.getBlueGreenStrategy())) {
            Map<String, ConditionAndRouteJson> conditionRouteMap = JsonUtil.fromJson(blueGreenDto.getBlueGreenStrategy(), new TypeReference<Map<String, ConditionAndRouteJson>>() {
            });

            List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = new ArrayList<>();
            List<StrategyRouteEntity> strategyRouteEntityList = new ArrayList<>();
            conditionAndRoute.setConditionBlueGreenEntityList(strategyConditionBlueGreenEntityList);
            conditionAndRoute.setStrategyRouteEntityList(strategyRouteEntityList);

            int index = 0;
            for (Map.Entry<String, ConditionAndRouteJson> pair : conditionRouteMap.entrySet()) {
                List<Map<String, String>> conditionList = pair.getValue().getCondition();
                List<Map<String, String>> routeList = pair.getValue().getRoute();

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
                strategyConditionBlueGreenEntity.setExpression(conditionList.get(0).get(PlatformConstant.SPEL_CONDITION));
                strategyConditionBlueGreenEntityList.add(strategyConditionBlueGreenEntity);

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
                strategyRouteEntity.setValue(toServiceJson(JsonUtil.toJson(routeList)));
                strategyRouteEntityList.add(strategyRouteEntity);

                index++;
            }
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