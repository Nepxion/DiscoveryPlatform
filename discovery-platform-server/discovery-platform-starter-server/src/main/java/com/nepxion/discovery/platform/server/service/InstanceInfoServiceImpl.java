package com.nepxion.discovery.platform.server.service;

import com.google.common.collect.Lists;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.ServerMetricInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: discovery-platform
 * @description:
 * @author: xiaolong
 * @create: 2021-07-17 10:18
 **/

@Service
public class InstanceInfoServiceImpl implements InstanceInfoService {

    public static final Logger LOG = LoggerFactory.getLogger(InstanceInfoServiceImpl.class);

    @Resource
    private ServiceResource serviceResource;
    private static Integer currentPage;
    private static Integer size;


    @Override
    public Result<List<ServerMetricInfoVo>> search(ListSearchNamePo listSearchNamePo) {
        List<ServerMetricInfoVo> serverMetricInfoVoList = Lists.newArrayList();
        currentPage = listSearchNamePo.getPage();
        size = listSearchNamePo.getLimit();
        if (StringUtils.isEmpty(listSearchNamePo.getName())) {
            List<String> serviceNames = serviceResource.getServices();
            if (serviceNames.size() == 0) {
                return Result.ok(Collections.emptyList(), serviceNames.size());
            }
            serviceNames.stream().forEach(name -> {
                ServerMetricInfoVo serverMetric = new ServerMetricInfoVo();
                List<ServerMetricInfoVo.InstanceInfo> instanceInfos = Lists.newArrayList();
                List<InstanceEntity> instanceList = serviceResource.getInstanceList(name);
                if (instanceList.size() == 0) {
                    Result.ok(Collections.EMPTY_LIST, Collections.EMPTY_LIST.size());
                }
                getInstanceInfoVos(instanceInfos,instanceList);
                serverMetric.setServiceId(name);
                serverMetric.setInstanceInfos(instanceInfos);
                serverMetric.setInstanceNum(instanceInfos.size());
                serverMetric.setServiceType(instanceInfos.get(0).getServiceType());
                serverMetricInfoVoList.add(serverMetric);
            });
            List<ServerMetricInfoVo> serverMetricInfoVos = serverMetricInfoVoList.stream()
                    .skip((currentPage - 1) * size).limit(size).collect(Collectors.toList());

            return Result.ok(serverMetricInfoVos, serverMetricInfoVoList.size());
        }
        ServerMetricInfoVo serverMetric = new ServerMetricInfoVo();
        List<InstanceEntity> instanceList = serviceResource.getInstanceList(listSearchNamePo.getName());
        if (instanceList.size() == 0) {
           return Result.ok(Collections.EMPTY_LIST, Collections.EMPTY_LIST.size());
        }
        List<ServerMetricInfoVo.InstanceInfo> instanceInfos = Lists.newArrayList();
        getInstanceInfoVos(instanceInfos, instanceList);
        serverMetric.setServiceId(listSearchNamePo.getName());
        serverMetric.setInstanceInfos(instanceInfos);
        serverMetric.setInstanceNum(instanceInfos.size());
        serverMetric.setServiceType(instanceInfos.get(0).getServiceType());
        List<ServerMetricInfoVo> serverMetricInfoVos = serverMetricInfoVoList.stream()
                .skip((currentPage - 1) * size).limit(size).collect(Collectors.toList());
        return Result.ok(serverMetricInfoVos, serverMetricInfoVos.size());
    }

    private void getInstanceInfoVos(List<ServerMetricInfoVo.InstanceInfo> instanceInfos, List<InstanceEntity> instanceList) {
        for (InstanceEntity entity : instanceList) {
            String strInstance = JsonUtil.toJson(entity);
            ServerMetricInfoVo.InstanceInfo instanceInfo = JsonUtil.fromJson(strInstance, ServerMetricInfoVo.InstanceInfo.class);
            instanceInfos.add(instanceInfo);
        }
    }
}
