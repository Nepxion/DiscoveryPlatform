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

import java.util.Collection;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.BlacklistDto;

public interface BlacklistService {
    void publish() throws Exception;

    IPage<BlacklistDto> page(String description, Integer pageNum, Integer pageSize);

    BlacklistDto getById(Long id);

    void insert(BlacklistDto blacklistDto) throws Exception;

    void update(BlacklistDto blacklistDto);

    void enable(Long id, boolean enableFlag);

    void logicDelete(Collection<Long> ids);

    void delete(Collection<Long> ids);
}