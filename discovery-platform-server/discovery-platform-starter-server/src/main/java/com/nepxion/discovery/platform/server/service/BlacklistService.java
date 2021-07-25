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
import com.nepxion.discovery.platform.server.service.base.BasePublishService;

public interface BlacklistService extends BasePublishService<BlacklistDto> {
    void publish() throws Exception;

    IPage<BlacklistDto> page(String description, Integer pageNum, Integer pageSize);

    boolean insert(BlacklistDto blacklistDto) throws Exception;

    boolean logicDelete(Collection<Long> ids);

    boolean delete(Collection<Long> ids);
}