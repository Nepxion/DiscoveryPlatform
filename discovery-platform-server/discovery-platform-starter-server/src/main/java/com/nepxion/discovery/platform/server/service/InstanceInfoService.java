package com.nepxion.discovery.platform.server.service;

import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.ServerMetricInfoVo;

import java.util.List;

/**
 * @program: discovery-platform
 * @description:
 * @author: xiaolong
 * @create: 2021-07-17 10:16
 **/
public interface InstanceInfoService {
    /**
     * 实例信息分页查询
     * @param listSearchNamePo
     * @return
     */
    Result<List<ServerMetricInfoVo>> search(ListSearchNamePo listSearchNamePo);
}
