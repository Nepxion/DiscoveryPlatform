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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.service.base.BasePublishService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

public class PlatformPublishAdapter<M extends BaseMapper<T>, T extends BaseStateEntity> extends ServiceImpl<M, T> implements BasePublishService<T> {
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @TransactionReader
    public T getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

    @TransactionWriter
    public boolean enable(Serializable id, boolean enableFlag) throws Exception {
        T t = getById(id);
        if (t == null) {
            return false;
        }
        t.setEnableFlag(enableFlag);
        return update(t);
    }

    @TransactionWriter
    public boolean update(T t) throws Exception {
        t = prepareUpdate(t);
        if (t == null) {
            return false;
        }
        return updateById(t);
    }

    @TransactionWriter
    public boolean logicDelete(Collection<Long> ids) {
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
        return true;
    }

    @TransactionWriter
    public boolean delete(Collection<Long> ids) {
        return removeByIds(ids);
    }

    @TransactionWriter
    protected void publish(Collection<String> gatewayNameCollection, PublishAction<T> publishAction) throws Exception {
        List<T> toBePublishList = list();
        publish(gatewayNameCollection, toBePublishList, publishAction);
    }

    @TransactionWriter
    protected void publish(List<T> toBePublishList, PublishAction<T> publishAction) throws Exception {
        List<String> gatewayNameCollection = toBePublishList.stream().map(T::getPortalName).collect(Collectors.toList());
        publish(gatewayNameCollection, toBePublishList, publishAction);
    }


    @TransactionWriter
    protected void publish(Collection<String> portalNameCollection, List<T> toBePublishList, PublishAction<T> publishAction) throws Exception {
        Set<String> gatewayNameSet = new HashSet<>(portalNameCollection);

        if (CollectionUtils.isEmpty(toBePublishList)) {
            for (String gatewayName : gatewayNameSet) {
                publishAction.publishEmptyConfig(gatewayName, null);
            }
            return;
        }

        List<T> toUpdateList = new ArrayList<>(toBePublishList.size());
        List<T> toDeleteList = new ArrayList<>(toBePublishList.size());
        Map<String, List<T>> unusedMap = new HashMap<>(toBePublishList.size());
        Map<String, List<Object>> usedMap = new HashMap<>(toBePublishList.size());

        for (T item : toBePublishList) {
            if (item.getPublishFlag()) {
                continue;
            }
            if (item.getDeleteFlag()) {
                toDeleteList.add(item);
                CommonTool.addKVForList(unusedMap, item.getPortalName(), item);
                continue;
            }
            if (!item.getEnableFlag()) {
                toUpdateList.add(item);
                CommonTool.addKVForList(unusedMap, item.getPortalName(), item);
                continue;
            }
            toUpdateList.add(item);
            Object object = publishAction.process(item);
            CommonTool.addKVForList(usedMap, item.getPortalName(), object);
        }

        for (Map.Entry<String, List<T>> entry : unusedMap.entrySet()) {
            publishAction.publishEmptyConfig(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, List<Object>> entry : usedMap.entrySet()) {
            String gatewayName = entry.getKey();
            publishAction.publishConfig(gatewayName, entry.getValue());
        }

        if (!CollectionUtils.isEmpty(toDeleteList)) {
            delete(toDeleteList.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
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

    protected void publishConfig(BaseStateEntity.PortalType portalType, String portalName, RuleEntity ruleEntity) throws Exception {
        switch (Objects.requireNonNull(portalType)) {
            case GATEWAY:
            case SERVICE:
                platformDiscoveryAdapter.publishConfig(portalName, ruleEntity);
                break;
            case GROUP:
                platformDiscoveryAdapter.publishConfig(portalName, portalName, ruleEntity);
                break;
        }
    }

    public interface PublishAction<T extends BaseStateEntity> {
        Object process(T t) throws Exception;

        void publishEmptyConfig(String portalName, List<T> entityList) throws Exception;

        void publishConfig(String portalName, List<Object> configList) throws Exception;
    }
}