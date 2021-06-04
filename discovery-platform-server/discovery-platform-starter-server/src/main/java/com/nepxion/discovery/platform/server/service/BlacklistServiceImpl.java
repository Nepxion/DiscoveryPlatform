package com.nepxion.discovery.platform.server.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.platform.server.entity.dto.BlacklistDto;
import com.nepxion.discovery.platform.server.entity.po.BlacklistPo;
import com.nepxion.discovery.platform.server.mapper.BlacklistMapper;
import com.nepxion.discovery.plugin.framework.parser.PluginConfigDeparser;
import com.nepxion.discovery.plugin.framework.parser.PluginConfigParser;

public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, BlacklistDto> implements BlacklistService {
    @Autowired
    private PluginConfigParser pluginConfigParser;

    @Autowired
    private PluginConfigDeparser pluginConfigDeparser;

    @Override
    public void publish() throws Exception {
        // 由于蓝绿灰度规则和黑名单规则都保存在同一个xml里，保存之前必须先从配置中心拉一次全量规则，以免把灰度规则给冲掉
        RuleEntity ruleEntity = pluginConfigParser.parse("");
        
        ruleEntity.setStrategyBlacklistEntity(new BlacklistPo());

        String config = pluginConfigDeparser.deparse(ruleEntity);
        
        // 推送到配置中心
    }

    @Override
    public IPage<BlacklistDto> page(String description, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public BlacklistDto getById(Long id) {
        return null;
    }

    @Override
    public void insert(BlacklistDto blacklistDto) {

    }

    @Override
    public void update(BlacklistDto blacklistDto) {

    }

    @Override
    public void enable(Long id, boolean enableFlag) {

    }

    @Override
    public void logicDelete(Collection<Long> ids) {

    }

    @Override
    public void delete(Collection<Long> ids) {

    }
}