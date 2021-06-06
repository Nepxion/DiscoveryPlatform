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
                    public void publishEmptyConfig(String gatewayName) throws Exception {
                        RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(gatewayName);
                        ruleEntity.setStrategyBlacklistEntity(new StrategyBlacklistEntity());
                        platformDiscoveryAdapter.publishConfig(gatewayName, platformDiscoveryAdapter.ruleEntityToXml(ruleEntity));
                    }

                    @Override
                    public void publishConfig(String gatewayName, List<Object> configList) throws Exception {
                        RuleEntity ruleEntity = platformDiscoveryAdapter.getConfig(gatewayName);
                        StrategyBlacklistEntity strategyBlacklistEntity = new StrategyBlacklistEntity();
                        strategyBlacklistEntity.setIdValue(null);
                        strategyBlacklistEntity.setAddressValue(null);

                        Map<String, Set<String>> uuidMap = new LinkedHashMap<>(configList.size());
                        Map<String, Set<String>> addressMap = new LinkedHashMap<>(configList.size());

                        for (Object item : configList) {
                            BlacklistDto blacklistDto = (BlacklistDto) item;
                            if (!StringUtils.isEmpty(blacklistDto.getServiceUUID())) {
                                CommonTool.addKVForSet(uuidMap, blacklistDto.getGatewayName(), blacklistDto.getServiceUUID());
                            }
                            if (!StringUtils.isEmpty(blacklistDto.getServiceAddress())) {
                                CommonTool.addKVForSet(addressMap, blacklistDto.getGatewayName(), blacklistDto.getServiceAddress());
                            }
                        }

                        if (!CollectionUtils.isEmpty(uuidMap)) {
                            Map<String, List<String>> map = new LinkedHashMap<>();
                            for (Map.Entry<String, Set<String>> pair : uuidMap.entrySet()) {
                                map.put(pair.getKey(), new ArrayList<>(pair.getValue()));
                            }
                            strategyBlacklistEntity.setIdValue(StringUtil.convertToComplexString(map));
                        }

                        if (!CollectionUtils.isEmpty(addressMap)) {
                            Map<String, List<String>> map = new LinkedHashMap<>();
                            for (Map.Entry<String, Set<String>> pair : addressMap.entrySet()) {
                                map.put(pair.getKey(), new ArrayList<>(pair.getValue()));
                            }
                            strategyBlacklistEntity.setAddressValue(StringUtil.convertToComplexString(map));
                        }
                        ruleEntity.setStrategyBlacklistEntity(strategyBlacklistEntity);
                        platformDiscoveryAdapter.publishConfig(gatewayName, ruleEntity);
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

        List<Map<String, String>> uuidList = JsonUtil.fromJson(blacklistDto.getServiceUUID(), List.class);
        for (Map<String, String> map : uuidList) {
            for (Map.Entry<String, String> pair : map.entrySet()) {
                BlacklistDto item = prepareInsert(new BlacklistDto());
                item.setServiceName(pair.getKey());
                item.setServiceUUID(pair.getValue());
                item.setGatewayName(blacklistDto.getGatewayName());
                item.setDescription(blacklistDto.getDescription());
                item.setServiceAddress(StringUtils.EMPTY);
                blacklistDtoList.add(item);
            }
        }

        List<Map<String, String>> addressList = JsonUtil.fromJson(blacklistDto.getServiceAddress(), List.class);
        for (Map<String, String> map : addressList) {
            for (Map.Entry<String, String> pair : map.entrySet()) {
                BlacklistDto item = prepareInsert(new BlacklistDto());
                item.setServiceName(pair.getKey());
                item.setServiceAddress(pair.getValue());
                item.setGatewayName(blacklistDto.getGatewayName());
                item.setDescription(blacklistDto.getDescription());
                item.setServiceUUID(StringUtils.EMPTY);
                blacklistDtoList.add(item);
            }
        }
        saveBatch(blacklistDtoList);
    }
}