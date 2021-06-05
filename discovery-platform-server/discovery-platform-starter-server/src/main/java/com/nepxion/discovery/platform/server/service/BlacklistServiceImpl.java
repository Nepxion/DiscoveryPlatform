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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyBlacklistEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;
import com.nepxion.discovery.platform.server.entity.dto.BlacklistDto;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.mapper.BlacklistMapper;

public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, BlacklistDto> implements BlacklistService {
    @Autowired
    private DiscoveryService discoveryService;

    @Override
    public void publish() throws Exception {
        List<BlacklistDto> blacklistDtoList = list();
        if (CollectionUtils.isEmpty(blacklistDtoList)) {
            List<String> gatewayNameList = discoveryService.getGatewayNames();
            for (String gatewayName : gatewayNameList) {
                String group = discoveryService.getGroup(gatewayName);
                discoveryService.updateRemoteConfig(group, gatewayName, discoveryService.ruleEntityToXml(new RuleEntity()));
            }
            return;
        }
        List<BlacklistDto> toUpdateList = new ArrayList<>(blacklistDtoList.size());
        List<BlacklistDto> toDeleteList = new ArrayList<>(blacklistDtoList.size());
        Map<String, List<BlacklistDto>> unusedMap = new HashMap<>();

        Map<String, Set<String>> uuidMap = new LinkedHashMap<>();
        Map<String, Set<String>> addressMap = new LinkedHashMap<>();
        Map<String, List<BlacklistDto>> newBlackListMap = new HashMap<>();

        for (BlacklistDto blacklistDto : blacklistDtoList) {
            if (blacklistDto.getDeleteFlag()) {
                toDeleteList.add(blacklistDto);
                addKV(unusedMap, blacklistDto.getGatewayName(), blacklistDto);
                continue;
            } else if (!blacklistDto.getEnableFlag()) {
                toUpdateList.add(blacklistDto);
                addKV(unusedMap, blacklistDto.getGatewayName(), blacklistDto);
                continue;
            }
            toUpdateList.add(blacklistDto);
            String gatewayName = blacklistDto.getGatewayName();

            if (newBlackListMap.containsKey(gatewayName)) {
                newBlackListMap.get(gatewayName).add(blacklistDto);
            } else {
                List<BlacklistDto> blacklistDtoArrayList = new ArrayList<>();
                blacklistDtoArrayList.add(blacklistDto);
                newBlackListMap.put(gatewayName, blacklistDtoArrayList);
            }
        }

        if (CollectionUtils.isEmpty(newBlackListMap)) {
            for (Map.Entry<String, List<BlacklistDto>> pair : unusedMap.entrySet()) {
                String gatewayName = pair.getKey();
                String group = discoveryService.getGroup(gatewayName);
                discoveryService.updateRemoteConfig(group, gatewayName, discoveryService.ruleEntityToXml(new RuleEntity()));
            }
        } else {
            for (Map.Entry<String, List<BlacklistDto>> entry : newBlackListMap.entrySet()) {
                String gatewayName = entry.getKey();
                for (BlacklistDto blacklistDto : entry.getValue()) {
                    if (!StringUtils.isEmpty(blacklistDto.getServiceUUID())) {
                        addKV(uuidMap, blacklistDto.getServiceName(), blacklistDto.getServiceUUID());
                    }
                    if (!StringUtils.isEmpty(blacklistDto.getServiceAddress())) {
                        addKV(addressMap, blacklistDto.getServiceName(), blacklistDto.getServiceAddress());
                    }
                }
                String group = discoveryService.getGroup(gatewayName);
                String remoteConfig = discoveryService.getRemoteConfig(group, gatewayName);
                RuleEntity ruleEntity;
                if (StringUtils.isEmpty(remoteConfig)) {
                    ruleEntity = new RuleEntity();
                } else {
                    ruleEntity = discoveryService.xmlToRuleEntity(remoteConfig);
                }
                StrategyBlacklistEntity strategyBlacklistEntity = new StrategyBlacklistEntity();
                strategyBlacklistEntity.setIdValue(null);
                strategyBlacklistEntity.setAddressValue(null);
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
                String config = discoveryService.ruleEntityToXml(ruleEntity);
                discoveryService.updateRemoteConfig(group, gatewayName, config);
            }
        }

        if (!CollectionUtils.isEmpty(toDeleteList)) {
            delete(toDeleteList.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }

        if (!CollectionUtils.isEmpty(toUpdateList)) {
            for (BlacklistDto blacklistDto : toUpdateList) {
                blacklistDto.setPublishFlag(true);
            }
            updateBatchById(toUpdateList, toUpdateList.size());
        }
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

    @TransactionReader
    @Override
    public BlacklistDto getById(Long id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

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
                BlacklistDto item = new BlacklistDto();
                item.setServiceName(pair.getKey());
                item.setServiceUUID(pair.getValue());
                item.setGatewayName(blacklistDto.getGatewayName());
                item.setDescription(blacklistDto.getDescription());
                item.setServiceAddress(StringUtils.EMPTY);
                item.setOperation(Operation.INSERT.getCode());
                item.setEnableFlag(true);
                item.setPublishFlag(false);
                item.setDeleteFlag(false);
                blacklistDtoList.add(item);
            }
        }

        List<Map<String, String>> addressList = JsonUtil.fromJson(blacklistDto.getServiceAddress(), List.class);
        for (Map<String, String> map : addressList) {
            for (Map.Entry<String, String> pair : map.entrySet()) {
                BlacklistDto item = new BlacklistDto();
                item.setServiceName(pair.getKey());
                item.setServiceAddress(pair.getValue());
                item.setGatewayName(blacklistDto.getGatewayName());
                item.setDescription(blacklistDto.getDescription());
                item.setServiceUUID(StringUtils.EMPTY);
                item.setOperation(Operation.INSERT.getCode());
                item.setEnableFlag(true);
                item.setPublishFlag(false);
                item.setDeleteFlag(false);
                blacklistDtoList.add(item);
            }
        }

        super.saveBatch(blacklistDtoList);
    }

    @TransactionWriter
    @Override
    public void update(BlacklistDto blacklistDto) {
        if (blacklistDto == null) {
            return;
        }
        blacklistDto.setPublishFlag(false);
        blacklistDto.setDeleteFlag(false);
        blacklistDto.setOperation(Operation.UPDATE.getCode());
        updateById(blacklistDto);
    }

    @TransactionWriter
    @Override
    public void enable(Long id, boolean enableFlag) {
        BlacklistDto blacklistDto = getById(id);
        blacklistDto.setEnableFlag(enableFlag);
        update(blacklistDto);
    }

    @TransactionWriter
    @Override
    public void logicDelete(Collection<Long> ids) {
        for (Long id : ids) {
            BlacklistDto blacklistDto = getById(id);
            if (blacklistDto == null) {
                continue;
            }
            blacklistDto.setDeleteFlag(true);
            blacklistDto.setPublishFlag(false);
            blacklistDto.setOperation(Operation.DELETE.getCode());
            updateById(blacklistDto);
        }
    }

    @TransactionWriter
    @Override
    public void delete(Collection<Long> ids) {
        super.removeByIds(ids);
    }

    private void addKV(Map<String, Set<String>> map, String serviceName, String value) {
        if (map.containsKey(serviceName)) {
            map.get(serviceName).add(value);
        } else {
            Set<String> sets = new LinkedHashSet<>();
            sets.add(value);
            map.put(serviceName, sets);
        }
    }

    private void addKV(Map<String, List<BlacklistDto>> map, String key, BlacklistDto blacklistDto) {
        if (map.containsKey(key)) {
            map.get(key).add(blacklistDto);
        } else {
            List<BlacklistDto> blacklistDtoList = new ArrayList<>();
            blacklistDtoList.add(blacklistDto);
            map.put(key, blacklistDtoList);
        }
    }
}