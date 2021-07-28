package com.nepxion.discovery.platform.server.controller;

import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.ServerMetricInfoVo;
import com.nepxion.discovery.platform.server.service.InstanceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: discovery-platform
 * @description: instance manager
 * @author: xiaolong
 * @create: 2021-07-17 09:38
 **/

@Api("实例信息相关接口")
@RestController
@RequestMapping(InstanceInfoController.PREFIX)
public class InstanceInfoController {

    @Resource
    private InstanceInfoService instanceInfoService;

    public static final String PREFIX = "instance";

    @ApiOperation("获取实例信息列表")
    @PostMapping("do-list")
    public Result<List<ServerMetricInfoVo>> doList(ListSearchNamePo listSearchNamePo) {
        return instanceInfoService.search(listSearchNamePo);
    }

}
