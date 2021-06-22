package com.nepxion.discovery.platform.server.adapter;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.service.base.BasePublishService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

public class PlatformPublishAdapter<M extends BaseMapper<T>, T extends BaseStateEntity> extends ServiceImpl<M, T> implements BasePublishService<T> {
    @TransactionReader
    public T getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

    @TransactionWriter
    public void enable(Serializable id, boolean enableFlag) {
        T t = getById(id);
        t.setEnableFlag(enableFlag);
        update(t);
    }

    @TransactionWriter
    public void update(T t) {
        t = prepareUpdate(t);
        if (t == null) {
            return;
        }
        updateById(t);
    }

    @TransactionWriter
    public void logicDelete(Collection<Long> ids) {
        for (Long id : ids) {
            T t = getById(id);
            if (t == null) {
                continue;
            }
            t.setDeleteFlag(true);
            t.setPublishFlag(false);
            t.setOperation(Operation.DELETE.getCode());
            updateById(t);
        }
    }

    @TransactionWriter
    public void delete(Collection<Long> ids) {
        removeByIds(ids);
    }

    @TransactionWriter
    protected void publish(Collection<String> gatewayNameCollection, PublishAction<T> publishAction) throws Exception {
        Set<String> gatewayNameSet = new HashSet<>(gatewayNameCollection);
        List<T> toBePublishList = list();

        if (CollectionUtils.isEmpty(toBePublishList)) {
            for (String gatewayName : gatewayNameSet) {
                publishAction.publishEmptyConfig(gatewayName);
            }
            return;
        }

        List<T> toUpdateList = new ArrayList<>(toBePublishList.size());
        List<T> toDeleteList = new ArrayList<>(toBePublishList.size());
        Map<String, List<T>> unusedMap = new HashMap<>(toBePublishList.size());
        Map<String, List<Object>> usedMap = new HashMap<>(toBePublishList.size());

        for (T item : toBePublishList) {
            if (item.getDeleteFlag()) {
                toDeleteList.add(item);
                CommonTool.addKVForList(unusedMap, item.getGatewayName(), item);
                continue;
            }
            if (!item.getEnableFlag()) {
                toUpdateList.add(item);
                CommonTool.addKVForList(unusedMap, item.getGatewayName(), item);
                continue;
            }
            toUpdateList.add(item);
            Object object = publishAction.process(item);
            CommonTool.addKVForList(usedMap, item.getGatewayName(), object);
        }

        if (CollectionUtils.isEmpty(usedMap)) {
            for (Map.Entry<String, List<T>> entry : unusedMap.entrySet()) {
                String gatewayName = entry.getKey();
                publishAction.publishEmptyConfig(gatewayName);
            }
        } else {
            for (Map.Entry<String, List<Object>> entry : usedMap.entrySet()) {
                String gatewayName = entry.getKey();
                publishAction.publishConfig(gatewayName, entry.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(toDeleteList)) {
            removeByIds(toDeleteList.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }
        if (!CollectionUtils.isEmpty(toUpdateList)) {
            for (T t : toUpdateList) {
                t.setPublishFlag(true);
            }
            updateBatchById(toUpdateList, toUpdateList.size());
        }
    }

    protected T prepareInsert(T t) {
        if (t == null) {
            return null;
        }
        t.setEnableFlag(true);
        t.setPublishFlag(false);
        t.setDeleteFlag(false);
        t.setOperation(Operation.INSERT.getCode());
        return t;
    }

    protected T prepareUpdate(T t) {
        if (t == null) {
            return null;
        }
        t.setPublishFlag(false);
        t.setDeleteFlag(false);
        t.setOperation(Operation.UPDATE.getCode());
        return t;
    }

    public interface PublishAction<T extends BaseStateEntity> {
        Object process(T t) throws Exception;

        void publishEmptyConfig(String gatewayName) throws Exception;

        void publishConfig(String gatewayName, List<Object> configList) throws Exception;
    }
}