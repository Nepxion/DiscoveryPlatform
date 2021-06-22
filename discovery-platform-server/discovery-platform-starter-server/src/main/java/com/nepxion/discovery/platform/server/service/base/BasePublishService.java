package com.nepxion.discovery.platform.server.service.base;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.io.Serializable;

import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;

public interface BasePublishService<T extends BaseStateEntity> {
    T getById(Serializable id);

    void update(T t);

    void enable(Serializable id, boolean enableFlag);
}