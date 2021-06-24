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
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyEntity;
import com.nepxion.discovery.common.entity.StrategyHeaderEntity;
import com.nepxion.discovery.common.entity.StrategyReleaseEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import com.nepxion.discovery.platform.server.entity.po.BlueGreenPo;
import com.nepxion.discovery.platform.server.mapper.BlueGreenMapper;

public class BlueGreenServiceImpl extends PlatformPublishAdapter<BlueGreenMapper, BlueGreenDto> implements BlueGreenService {

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

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
                    }

                    @Override
                    public void publishConfig(String portalName, List<Object> configList) throws Exception {
                        for (Object item : configList) {
                            RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                            StrategyEntity strategyEntity = new StrategyEntity();
                            StrategyReleaseEntity strategyReleaseEntity = new StrategyReleaseEntity();
                            if (ruleEntity.getStrategyReleaseEntity() != null) {
                                strategyReleaseEntity.setStrategyConditionGrayEntityList(ruleEntity.getStrategyReleaseEntity().getStrategyConditionGrayEntityList());
                            }

                            BlueGreenDto blueGreenDto = (BlueGreenDto) item;
                            BaseStateEntity.PortalType portalType = BaseStateEntity.PortalType.get(blueGreenDto.getPortalType());
                            BlueGreenDto.Type type = BlueGreenDto.Type.get(blueGreenDto.getType());

                            if (StringUtils.isNotEmpty(blueGreenDto.getStrategy())) {
                                String strategy = toServiceJson(blueGreenDto.getStrategy());
                                switch (Objects.requireNonNull(type)) {
                                    case REGION:
                                        strategyEntity.setRegionValue(strategy);
                                        break;
                                    case VERSION:
                                        strategyEntity.setVersionValue(strategy);
                                        break;
                                }
                            }

                            if (StringUtils.isNotEmpty(blueGreenDto.getHeader())) {
                                StrategyHeaderEntity strategyHeaderEntity = new StrategyHeaderEntity();
                                strategyHeaderEntity.setHeaderMap(toMap(blueGreenDto.getHeader()));
                                strategyReleaseEntity.setStrategyHeaderEntity(strategyHeaderEntity);
                            }

                            if (StringUtils.isNotEmpty(blueGreenDto.getCondition())) {
                                // <condition id="blue-condition" expression="#H['a'] == '1'" version-id|region-id="blue-version-route"/>

                                //{"condition1":[{"parameterName":"b","operator":"!=","value":"2","spelCondition":"#H['b'] != '2' "}],"condition2":[{"parameterName":"c","operator":">","value":"3","spelCondition":"#H['c'] > '3' "}]}
                                List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = new ArrayList<>();
                                // TODO
                                strategyReleaseEntity.setStrategyConditionBlueGreenEntityList(strategyConditionBlueGreenEntityList);
                            }

                            if (StringUtils.isNotEmpty(blueGreenDto.getRoute())) {
                                //<route id="blue-version-route" type="version|region">{"discovery-guide-service-a":"1.1", "discovery-guide-service-b":"1.1"}</route>

                                //{"route1":[{"serviceName":"discovery-guide-service-b","value":"1.0"}],"route2":[{"serviceName":"discovery-guide-service-a","value":"1.0"}]}
                                List<StrategyRouteEntity> strategyRouteEntityList = new ArrayList<>();
                                // TODO

                                strategyReleaseEntity.setStrategyRouteEntityList(strategyRouteEntityList);
                            }

                            ruleEntity.setStrategyEntity(strategyEntity);
                            ruleEntity.setStrategyReleaseEntity(strategyReleaseEntity);
                            BlueGreenServiceImpl.super.publishConfig(portalType, portalName, ruleEntity);
                        }

                    }
                }
        );
    }

    @Override
    public IPage<BlueGreenDto> list(String name, Integer page, Integer limit) {
        LambdaQueryWrapper<BlueGreenDto> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like(BlueGreenDto::getDescription, name);
        }
        queryWrapper.orderByAsc(BlueGreenDto::getCreateTime);
        return this.page(new Page<>(page, limit), queryWrapper);
    }

    @Override
    public Boolean insert(BlueGreenPo blueGreenPo) {
        BlueGreenDto blueGreenDto = prepareInsert(new BlueGreenDto());
        blueGreenDto.setPortalName(blueGreenPo.getPortalName());
        blueGreenDto.setPortalType(blueGreenPo.getPortalType());
        blueGreenDto.setType(blueGreenPo.getType());
        blueGreenDto.setStrategy(blueGreenPo.getStrategy());
        blueGreenDto.setCondition(blueGreenPo.getCondition());
        blueGreenDto.setRoute(blueGreenPo.getRoute());
        blueGreenDto.setHeader(blueGreenPo.getHeader());
        blueGreenDto.setDescription(blueGreenPo.getDescription());
        return save(blueGreenDto);
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
}