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
import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import com.nepxion.discovery.platform.server.entity.po.BlueGreenPo;
import com.nepxion.discovery.platform.server.service.base.BasePublishService;

public interface BlueGreenService extends BasePublishService<BlueGreenDto> {
    void publish() throws Exception;

    IPage<BlueGreenDto> page(String name, Integer page, Integer limit);

    Boolean insert(BlueGreenPo blueGreenPo);

    Boolean update(BlueGreenPo blueGreenPo);

    void logicDelete(Collection<Long> ids);

    void delete(Collection<Long> ids);

    List<String> listPortalNames();
}