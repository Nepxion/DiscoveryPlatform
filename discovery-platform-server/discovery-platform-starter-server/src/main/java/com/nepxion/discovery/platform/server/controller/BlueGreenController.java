package com.nepxion.discovery.platform.server.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.BlueGreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("蓝绿发布相关接口")
@RestController
@RequestMapping(BlueGreenController.PREFIX)
public class BlueGreenController {
    public static final String PREFIX = "blue-green";

    @Autowired
    private BlueGreenService blueGreenService;
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @ApiOperation("获取蓝绿发布信息列表")
    @PostMapping("do-list")
    public Result<List<BlueGreenDto>> doList(ListSearchNamePo listSearchNamePo) {
        IPage<BlueGreenDto> blueGreenDtoPage = blueGreenService.list(listSearchNamePo.getName(), listSearchNamePo.getPage(), listSearchNamePo.getLimit());
        return Result.ok(blueGreenDtoPage.getRecords(), blueGreenDtoPage.getTotal());
    }

    @ApiOperation("通过服务名称获取所有该服务的信息")
    @PostMapping("do-list-service-metadata")
    public Result<List<InstanceEntity>> doListServiceMetadata(@RequestParam("serviceName") String serviceName) {
        if (StringUtils.isEmpty(serviceName)) {
            return Result.ok(new ArrayList<>());
        }
        return Result.ok(platformDiscoveryAdapter.getInstanceList(serviceName));
    }
}