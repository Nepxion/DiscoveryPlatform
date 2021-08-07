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
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.RouteArrangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("管理员相关接口")
@RestController
@RequestMapping(CommonController.PREFIX)
public class CommonController {
    public static final String PREFIX = "common";

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;
    @Autowired
    private RouteArrangeService routeArrangeService;

    @ApiOperation("获取所有链路编排的信息")
    @PostMapping("do-list-route-arrange")
    public Result<List<RouteArrangeDto>> doListRouteNames(@RequestParam("strategyType") Integer strategyType) {
        return Result.ok(routeArrangeService.list(strategyType));
    }

    @ApiOperation("获取所有Spring Cloud Gateway网关名称")
    @PostMapping("do-list-gateway-names")
    public Result<List<String>> doListGatewayNames() {
        return Result.ok(platformDiscoveryAdapter.getGatewayNames());
    }


    @ApiOperation("获取所有Spring Cloud Gateway服务名称")
    @PostMapping("do-list-service-names")
    public Result<List<String>> doListServiceNames() {
        return Result.ok(platformDiscoveryAdapter.getServiceNames());
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