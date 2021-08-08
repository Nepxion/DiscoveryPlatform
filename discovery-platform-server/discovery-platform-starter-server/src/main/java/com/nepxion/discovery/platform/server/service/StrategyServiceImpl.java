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
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.entity.RegionWeightEntity;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyBlacklistEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
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
import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.entity.dto.RouteStrategyDto;
import com.nepxion.discovery.platform.server.entity.dto.StrategyDto;
import com.nepxion.discovery.platform.server.entity.po.StrategyPo;
import com.nepxion.discovery.platform.server.mapper.StrategyMapper;

public class StrategyServiceImpl extends PlatformPublishAdapter<StrategyMapper, StrategyDto> implements StrategyService {
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Lazy
    @Autowired
    private RouteArrangeService routeArrangeService;

    @Autowired
    private RouteStrategyService routeStrategyService;

    @TransactionWriter
    @Override
    public void publish() throws Exception {
        List<StrategyDto> strategyDtoList = getUnPublish();

        this.publish(strategyDtoList, new PublishAction<StrategyDto>() {
            @Override
            public Object process(StrategyDto strategyDto) {
                return strategyDto;
            }

            @Override
            public void publishEmptyConfig(String portalName, List<StrategyDto> strategyDtoList) throws Exception {
                RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                ruleEntity.setStrategyBlacklistEntity(new StrategyBlacklistEntity());
                if (CollectionUtils.isEmpty(strategyDtoList)) {
                    platformDiscoveryAdapter.publishConfig(portalName, ruleEntity);
                } else {
                    Set<BaseStateEntity.PortalType> portalTypeSet = new HashSet<>();
                    for (StrategyDto strategyDto : strategyDtoList) {
                        portalTypeSet.add(BaseStateEntity.PortalType.get(strategyDto.getPortalType()));
                    }
                    for (BaseStateEntity.PortalType portalType : portalTypeSet) {
                        StrategyServiceImpl.super.publishConfig(portalType, portalName, ruleEntity);
                    }
                }
            }

            @Override
            public void publishConfig(String portalName, List<Object> configList) throws Exception {
                for (Object object : configList) {
                    StrategyDto strategyDto = (StrategyDto) object;
                    Set<String> routeIdSet = new HashSet<>();

                    RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(strategyDto.getPortalName());
                    StrategyReleaseEntity strategyReleaseEntity = new StrategyReleaseEntity();
                    strategyReleaseEntity.setStrategyConditionBlueGreenEntityList(generateConditionBlueGreen(strategyDto, routeIdSet));
                    strategyReleaseEntity.setStrategyConditionGrayEntityList(generateConditionGray(strategyDto, routeIdSet));
                    strategyReleaseEntity.setStrategyRouteEntityList(generateStrategyRoute(strategyDto, routeIdSet));
                    strategyReleaseEntity.setStrategyHeaderEntity(generateHeader(strategyDto));


                    if (strategyReleaseEntity.getStrategyConditionBlueGreenEntityList().size() > 0 ||
                            strategyReleaseEntity.getStrategyConditionGrayEntityList().size() > 0 ||
                            strategyReleaseEntity.getStrategyRouteEntityList().size() > 0 ||
                            strategyReleaseEntity.getStrategyHeaderEntity().getHeaderMap().size() > 0) {
                        ruleEntity.setStrategyReleaseEntity(strategyReleaseEntity);
                    }
                    ruleEntity.setStrategyEntity(generateGlobalStrategy(strategyDto));
                    StrategyServiceImpl.super.publishConfig(BaseStateEntity.PortalType.get(strategyDto.getPortalType()), strategyDto.getPortalName(), ruleEntity);
                }
            }
        });
        routeArrangeService.updatePublish(true);
        routeArrangeService.removeWithLogicDeleteIsTrue();
    }

    @TransactionReader
    @Override
    public IPage<StrategyDto> page(String name, Integer page, Integer limit) {
        LambdaQueryWrapper<StrategyDto> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like(StrategyDto::getDescription, name);
        }
        queryWrapper.orderByAsc(StrategyDto::getCreateTime);
        return this.page(new Page<>(page, limit), queryWrapper);
    }

    @TransactionReader
    @Override
    public List<StrategyDto> getUnPublish() {
        return baseMapper.getUnPublish();
    }

    @TransactionWriter
    @Override
    public boolean insert(StrategyPo strategyPo) {
        routeStrategyService.save(strategyPo.getPortalName(), strategyPo.getPortalType(), JsonUtil.fromJson(strategyPo.getRouteIds(), new TypeReference<List<String>>() {
        }));
        StrategyDto strategyDto = prepareInsert(new StrategyDto());
        strategyDto.setPortalName(strategyPo.getPortalName());
        strategyDto.setPortalType(strategyPo.getPortalType());
        strategyDto.setStrategyType(strategyPo.getStrategyType());
        strategyDto.setBasicGlobalStrategyRouteId(strategyPo.getBasicGlobalStrategyRouteId());
        strategyDto.setBasicBlueGreenStrategyRouteId(strategyPo.getBasicBlueGreenStrategyRouteId());
        strategyDto.setBlueGreenStrategy(strategyPo.getBlueGreenStrategy());
        strategyDto.setBasicGrayStrategy(strategyPo.getBasicGrayStrategy());
        strategyDto.setGrayStrategy(strategyPo.getGrayStrategy());
        strategyDto.setHeader(strategyPo.getHeader());
        strategyDto.setDescription(strategyPo.getDescription());
        return save(strategyDto);
    }

    @TransactionWriter
    @Override
    public boolean update(StrategyPo strategyPo) {
        StrategyDto strategyDto = prepareUpdate(this.getById(strategyPo.getId()));
        if (strategyDto == null) {
            return false;
        }
        routeStrategyService.save(strategyDto.getPortalName(), strategyDto.getPortalType(), JsonUtil.fromJson(strategyPo.getRouteIds(), new TypeReference<List<String>>() {
        }));
        strategyDto.setBasicGlobalStrategyRouteId(strategyPo.getBasicGlobalStrategyRouteId());
        strategyDto.setBasicBlueGreenStrategyRouteId(strategyPo.getBasicBlueGreenStrategyRouteId());
        strategyDto.setBlueGreenStrategy(strategyPo.getBlueGreenStrategy());
        strategyDto.setBasicGrayStrategy(strategyPo.getBasicGrayStrategy());
        strategyDto.setGrayStrategy(strategyPo.getGrayStrategy());
        strategyDto.setHeader(strategyPo.getHeader());
        strategyDto.setDescription(strategyPo.getDescription());
        return updateById(strategyDto);
    }

    @TransactionWriter
    @Override
    public boolean updatePublishFlag(String portalName, boolean flag) {
        LambdaUpdateWrapper<StrategyDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(StrategyDto::getPortalName, portalName)
                .set(StrategyDto::getPublishFlag, flag);
        return update(updateWrapper);
    }

    @TransactionReader
    @Override
    public List<String> listPortalNames() {
        return baseMapper.listPortalNames();
    }

    @TransactionReader
    public StrategyEntity generateGlobalStrategy(StrategyDto strategyDto) {
        StrategyEntity strategyEntity = new StrategyEntity();
        String basicGlobalStrategyRouteId = strategyDto.getBasicGlobalStrategyRouteId();
        if (StringUtils.isNotEmpty(basicGlobalStrategyRouteId)) {
            RouteArrangeDto.StrategyType strategyType = RouteArrangeDto.StrategyType.get(strategyDto.getStrategyType());
            RouteArrangeDto routeArrangeDto = routeArrangeService.getByRouteId(basicGlobalStrategyRouteId);
            switch (strategyType) {
                case VERSION:
                    strategyEntity.setVersionValue(generateRouteArrange(routeArrangeDto));
                    break;
                case REGION:
                    strategyEntity.setRegionValue(generateRouteArrange(routeArrangeDto));
                    break;
            }
        }
        return strategyEntity;
    }

    @TransactionReader
    public List<StrategyConditionBlueGreenEntity> generateConditionBlueGreen(StrategyDto strategyDto, Set<String> routeIdSet) {
        List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = new ArrayList<>();
        RouteArrangeDto.StrategyType strategyType = RouteArrangeDto.StrategyType.get(strategyDto.getStrategyType());
        if (strategyType == null) {
            return null;
        }

        String blueGreenStrategy = strategyDto.getBlueGreenStrategy();
        String basicBlueGreenStrategyRouteId = strategyDto.getBasicBlueGreenStrategyRouteId();

        if (StringUtils.isNotEmpty(blueGreenStrategy)) {
            int index = 0;
            Map<String, ConditionAndRoutePo> conditionAndRoutePoMap = JsonUtil.fromJson(blueGreenStrategy, new TypeReference<Map<String, ConditionAndRoutePo>>() {
            });

            for (Map.Entry<String, ConditionAndRoutePo> entry : conditionAndRoutePoMap.entrySet()) {
                StrategyConditionBlueGreenEntity strategyConditionBlueGreenEntity = new StrategyConditionBlueGreenEntity();
                strategyConditionBlueGreenEntityList.add(strategyConditionBlueGreenEntity);

                ConditionAndRoutePo conditionAndRoutePo = entry.getValue();
                String id = String.format(PlatformConstant.CONDITION, index);
                String expression = StringUtils.EMPTY;
                for (Map<String, String> conditionMap : conditionAndRoutePo.getCondition()) {
                    expression = conditionMap.get(PlatformConstant.SPEL_CONDITION);
                    break;
                }
                strategyConditionBlueGreenEntity.setId(id);
                strategyConditionBlueGreenEntity.setExpression(expression);
                routeIdSet.add(conditionAndRoutePo.getRouteId());
                switch (strategyType) {
                    case VERSION:
                        strategyConditionBlueGreenEntity.setVersionId(conditionAndRoutePo.getRouteId());
                        break;
                    case REGION:
                        strategyConditionBlueGreenEntity.setRegionId(conditionAndRoutePo.getRouteId());
                        break;
                }
                index++;
            }
        }

        if (StringUtils.isNotEmpty(basicBlueGreenStrategyRouteId)) {
            StrategyConditionBlueGreenEntity strategyConditionBlueGreenEntity = new StrategyConditionBlueGreenEntity();
            strategyConditionBlueGreenEntityList.add(strategyConditionBlueGreenEntity);
            strategyConditionBlueGreenEntity.setId(PlatformConstant.BASIC_CONDITION);
            routeIdSet.add(basicBlueGreenStrategyRouteId);
            switch (strategyType) {
                case VERSION:
                    strategyConditionBlueGreenEntity.setVersionId(basicBlueGreenStrategyRouteId);
                    break;
                case REGION:
                    strategyConditionBlueGreenEntity.setRegionId(basicBlueGreenStrategyRouteId);
                    break;
            }
        }

        return strategyConditionBlueGreenEntityList;
    }

    @TransactionReader
    public List<StrategyConditionGrayEntity> generateConditionGray(StrategyDto strategyDto, Set<String> routeIdSet) {
        List<StrategyConditionGrayEntity> strategyConditionGrayEntityList = new ArrayList<>();
        RouteArrangeDto.StrategyType strategyType = RouteArrangeDto.StrategyType.get(strategyDto.getStrategyType());
        if (strategyType == null) {
            return strategyConditionGrayEntityList;
        }

        String grayStrategy = strategyDto.getGrayStrategy();
        String basicGrayStrategy = strategyDto.getBasicGrayStrategy();
        if (StringUtils.isNotEmpty(grayStrategy)) {
            int index = 0;
            Map<String, ConditionAndRatePo> conditionAndRatePoMap = JsonUtil.fromJson(grayStrategy, new TypeReference<Map<String, ConditionAndRatePo>>() {
            });
            for (Map.Entry<String, ConditionAndRatePo> entry : conditionAndRatePoMap.entrySet()) {
                StrategyConditionGrayEntity strategyConditionGrayEntity = new StrategyConditionGrayEntity();
                strategyConditionGrayEntityList.add(strategyConditionGrayEntity);

                ConditionAndRatePo conditionAndRatePo = entry.getValue();
                String id = String.format(PlatformConstant.CONDITION, index);
                String expression = StringUtils.EMPTY;
                for (Map<String, String> conditionMap : conditionAndRatePo.getCondition()) {
                    expression = conditionMap.get(PlatformConstant.SPEL_CONDITION);
                    break;
                }
                strategyConditionGrayEntity.setId(id);
                strategyConditionGrayEntity.setExpression(expression);
                Map<String, Integer> weightEntity = new LinkedHashMap<>();
                for (Map<String, String> rateMap : conditionAndRatePo.getRate()) {
                    String routeId = rateMap.get(PlatformConstant.ROUTE_ID);
                    Integer rate = Integer.parseInt(rateMap.get(PlatformConstant.RATE));
                    routeIdSet.add(routeId);
                    weightEntity.put(routeId, rate);
                }
                switch (strategyType) {
                    case VERSION:
                        VersionWeightEntity versionWeightEntity = new VersionWeightEntity();
                        versionWeightEntity.setWeightMap(weightEntity);
                        strategyConditionGrayEntity.setVersionWeightEntity(versionWeightEntity);
                        break;
                    case REGION:
                        RegionWeightEntity regionWeightEntity = new RegionWeightEntity();
                        regionWeightEntity.setWeightMap(weightEntity);
                        strategyConditionGrayEntity.setRegionWeightEntity(regionWeightEntity);
                        break;
                }
                index++;
            }
        }

        if (StringUtils.isNotEmpty(basicGrayStrategy)) {
            List<Map<String, String>> routeIdRateMap = JsonUtil.fromJson(basicGrayStrategy, new TypeReference<List<Map<String, String>>>() {
            });

            StrategyConditionGrayEntity strategyConditionGrayEntity = new StrategyConditionGrayEntity();
            strategyConditionGrayEntityList.add(strategyConditionGrayEntity);
            strategyConditionGrayEntity.setId(PlatformConstant.BASIC_CONDITION);
            Map<String, Integer> weightEntity = new LinkedHashMap<>();
            for (Map<String, String> rateMap : routeIdRateMap) {
                String routeId = rateMap.get(PlatformConstant.ROUTE_ID);
                Integer rate = Integer.parseInt(rateMap.get(PlatformConstant.RATE));
                routeIdSet.add(routeId);
                weightEntity.put(routeId, rate);
            }
            switch (strategyType) {
                case VERSION:
                    VersionWeightEntity versionWeightEntity = new VersionWeightEntity();
                    versionWeightEntity.setWeightMap(weightEntity);
                    strategyConditionGrayEntity.setVersionWeightEntity(versionWeightEntity);
                    break;
                case REGION:
                    RegionWeightEntity regionWeightEntity = new RegionWeightEntity();
                    regionWeightEntity.setWeightMap(weightEntity);
                    strategyConditionGrayEntity.setRegionWeightEntity(regionWeightEntity);
                    break;
            }
        }

        return strategyConditionGrayEntityList;
    }

    @TransactionReader
    public List<StrategyRouteEntity> generateStrategyRoute(StrategyDto strategyDto, Set<String> routeIdSet) {
        List<StrategyRouteEntity> strategyRouteEntityList = new ArrayList<>();
        RouteArrangeDto.StrategyType strategyType = RouteArrangeDto.StrategyType.get(strategyDto.getStrategyType());
        if (strategyType == null) {
            return strategyRouteEntityList;
        }
        List<RouteStrategyDto> routeStrategyDtoList = routeStrategyService.getByPortalNameAndPortalType(strategyDto.getPortalName(), strategyDto.getPortalType());

        for (RouteStrategyDto routeStrategyDto : routeStrategyDtoList) {
            if (!routeIdSet.contains(routeStrategyDto.getRouteId())) {
                continue;
            }
            RouteArrangeDto routeArrangeDto = routeArrangeService.getByRouteId(routeStrategyDto.getRouteId());
            StrategyRouteEntity strategyRouteEntity = new StrategyRouteEntity();

            strategyRouteEntity.setId(routeStrategyDto.getRouteId());
            switch (strategyType) {
                case VERSION:
                    strategyRouteEntity.setType(StrategyRouteType.VERSION);
                    break;
                case REGION:
                    strategyRouteEntity.setType(StrategyRouteType.REGION);
                    break;
            }
            strategyRouteEntity.setValue(generateRouteArrange(routeArrangeDto));
            strategyRouteEntityList.add(strategyRouteEntity);
        }
        return strategyRouteEntityList;
    }

    @TransactionReader
    public StrategyHeaderEntity generateHeader(StrategyDto strategyDto) {
        StrategyHeaderEntity strategyHeaderEntity = new StrategyHeaderEntity();
        strategyHeaderEntity.setHeaderMap(toMap(strategyDto.getHeader()));
        return strategyHeaderEntity;
    }

    public static String generateRouteArrange(RouteArrangeDto routeArrangeDto) {
        List<Map<String, String>> mapList = JsonUtil.fromJson(routeArrangeDto.getServiceArrange(), new TypeReference<List<Map<String, String>>>() {
        });
        Map<String, String> result = new LinkedHashMap<>();
        for (Map<String, String> map : mapList) {
            result.putAll(map);
        }
        return JsonUtil.toJson(result);
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

    public static class ConditionAndRatePo {
        private List<Map<String, String>> condition;
        private List<Map<String, String>> rate;

        public List<Map<String, String>> getCondition() {
            return condition;
        }

        public void setCondition(List<Map<String, String>> condition) {
            this.condition = condition;
        }

        public List<Map<String, String>> getRate() {
            return rate;
        }

        public void setRate(List<Map<String, String>> rate) {
            this.rate = rate;
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object object) {
            return EqualsBuilder.reflectionEquals(this, object);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    public static class ConditionAndRoutePo {
        private List<Map<String, String>> condition;
        private String routeId;

        public List<Map<String, String>> getCondition() {
            return condition;
        }

        public void setCondition(List<Map<String, String>> condition) {
            this.condition = condition;
        }

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object object) {
            return EqualsBuilder.reflectionEquals(this, object);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }
}