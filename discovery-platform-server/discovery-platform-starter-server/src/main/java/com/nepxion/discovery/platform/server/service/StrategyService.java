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
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.StrategyDto;
import com.nepxion.discovery.platform.server.entity.po.StrategyPo;
import com.nepxion.discovery.platform.server.service.base.BasePublishService;

public interface StrategyService extends BasePublishService<StrategyDto> {
    void publish() throws Exception;

    IPage<StrategyDto> page(String name, Integer page, Integer limit);

    boolean insert(StrategyPo strategyPo);

    boolean update(StrategyPo strategyPo);

    boolean logicDelete(Collection<Long> ids);

    boolean delete(Collection<Long> ids);

    List<String> listPortalNames();

    boolean updatePublishFlag(String portalName, boolean flag);

    List<StrategyDto> getUnPublish();
}