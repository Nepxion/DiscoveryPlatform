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
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyBlacklistEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.BlacklistDto;
import com.nepxion.discovery.platform.server.mapper.BlacklistMapper;
import com.nepxion.discovery.platform.server.tool.CommonTool;

public class BlacklistServiceImpl extends PlatformPublishAdapter<BlacklistMapper, BlacklistDto> implements BlacklistService {
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Override
    public void publish() throws Exception {
        publish(platformDiscoveryAdapter.getGatewayNames(),
                new PublishAction<BlacklistDto>() {
                    @Override
                    public Object process(BlacklistDto blacklistDto) {
                        return blacklistDto;
                    }

                    @Override
                    public void publishEmptyConfig(String portalName) throws Exception {
                        RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                        ruleEntity.setStrategyBlacklistEntity(new StrategyBlacklistEntity());
                        platformDiscoveryAdapter.publishConfig(portalName, ruleEntity);
                    }

                    @Override
                    public void publishConfig(String portalName, List<Object> configList) throws Exception {
                        BaseStateEntity.PortalType portalType = null;
                        RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(portalName);
                        StrategyBlacklistEntity strategyBlacklistEntity = new StrategyBlacklistEntity();
                        strategyBlacklistEntity.setIdValue(null);
                        strategyBlacklistEntity.setAddressValue(null);

                        Map<String, Set<String>> uuidMap = new LinkedHashMap<>(configList.size());
                        Map<String, Set<String>> addressMap = new LinkedHashMap<>(configList.size());

                        for (Object item : configList) {
                            BlacklistDto blacklistDto = (BlacklistDto) item;
                            if (blacklistDto.getServiceBlacklistType() == BlacklistDto.Type.UUID.getCode()) {
                                CommonTool.addKVForSet(uuidMap, blacklistDto.getServiceName(), blacklistDto.getServiceBlacklist());
                            } else if (blacklistDto.getServiceBlacklistType() == BlacklistDto.Type.ADDRESS.getCode()) {
                                CommonTool.addKVForSet(addressMap, blacklistDto.getServiceName(), blacklistDto.getServiceBlacklist());
                            }
                            portalType = BaseStateEntity.PortalType.get(blacklistDto.getPortalType());
                        }
                        if (!CollectionUtils.isEmpty(uuidMap)) {
                            strategyBlacklistEntity.setIdValue(StringUtil.convertToComplexString(CommonTool.covertMapValuesFromSetToList(uuidMap)));
                        }

                        if (!CollectionUtils.isEmpty(addressMap)) {
                            strategyBlacklistEntity.setAddressValue(StringUtil.convertToComplexString(CommonTool.covertMapValuesFromSetToList(addressMap)));
                        }
                        ruleEntity.setStrategyBlacklistEntity(strategyBlacklistEntity);

                        switch (Objects.requireNonNull(portalType)) {
                            case GATEWAY:
                            case SERVICE:
                                platformDiscoveryAdapter.publishConfig(portalName, ruleEntity);
                                break;
                            case GROUP:
                                platformDiscoveryAdapter.publishConfig(portalName, portalName, ruleEntity);
                                break;
                        }

                    }
                }
        );
    }

    @TransactionReader
    @Override
    public IPage<BlacklistDto> page(String description, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BlacklistDto> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<BlacklistDto> lambda = queryWrapper.orderByAsc(BlacklistDto::getCreateTime);
        if (StringUtils.isNotEmpty(description)) {
            lambda.like(BlacklistDto::getDescription, description);
        }
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @SuppressWarnings("unchecked")
    @TransactionWriter
    @Override
    public void insert(BlacklistDto blacklistDto) throws Exception {
        if (blacklistDto == null) {
            return;
        }
        List<BlacklistDto> blacklistDtoList = new ArrayList<>();

        List<Map<String, String>> blacklistMap = JsonUtil.fromJson(blacklistDto.getServiceBlacklist(), List.class);
        for (Map<String, String> map : blacklistMap) {
            BlacklistDto item = prepareInsert(new BlacklistDto());
            item.setServiceName(map.get("serviceName"));
            item.setPortalName(blacklistDto.getPortalName());
            item.setPortalType(blacklistDto.getPortalType());
            item.setServiceBlacklistType(blacklistDto.getServiceBlacklistType());
            item.setDescription(blacklistDto.getDescription());
            if (blacklistDto.getServiceBlacklistType() == BlacklistDto.Type.UUID.getCode()) {
                item.setServiceBlacklist(map.get("uuid"));
            } else if (blacklistDto.getServiceBlacklistType() == BlacklistDto.Type.ADDRESS.getCode()) {
                item.setServiceBlacklist(map.get("address"));
            }
            blacklistDtoList.add(item);
        }
        saveBatch(blacklistDtoList);
    }
}