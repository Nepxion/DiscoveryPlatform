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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.entity.dto.BlacklistDto;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.mapper.BlacklistMapper;

public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, BlacklistDto> implements BlacklistService {
    @Override
    public void publish() throws Exception {

    }

    @Override
    public IPage<BlacklistDto> page(String description, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public RouteZuulDto getById(Long id) {
        return null;
    }

    @Override
    public void insert(BlacklistDto blacklistDto) {

    }

    @Override
    public void update(RouteZuulDto blacklistDto) {

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