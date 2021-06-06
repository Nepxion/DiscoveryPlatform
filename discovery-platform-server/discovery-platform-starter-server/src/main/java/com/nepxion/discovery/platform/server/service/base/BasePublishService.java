package com.nepxion.discovery.platform.server.service.base;

import java.io.Serializable;

import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;

public interface BasePublishService<T extends BaseStateEntity> {
    T getById(Serializable id);

    void update(T t);

    void enable(Serializable id, boolean enableFlag);
}