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
import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.service.base.BasePublishService;

public interface RouteArrangeService extends BasePublishService<RouteArrangeDto> {
    void publish() throws Exception;

    IPage<RouteArrangeDto> page(String description, Integer pageNum, Integer pageSize);

    boolean insert(RouteArrangeDto routeArrangeDto);

    boolean logicDelete(Collection<Long> ids);

    boolean delete(Collection<Long> ids);

    Long getNextIndex();

    List<RouteArrangeDto> list();

    RouteArrangeDto getByRouteId(String routeId);
}