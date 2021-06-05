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
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.entity.ZuulStrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.RouteResource;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchGatewayPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.RouteZuulVo;
import com.nepxion.discovery.platform.server.service.RouteZuulService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("Zuul动态路由接口")
@RestController
@RequestMapping(RouteZuulController.PREFIX)
public class RouteZuulController {
    public static final String PREFIX = "route-zuul";

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Autowired
    private RouteResource routeResource;

    @Autowired
    private RouteZuulService routeZuulService;

    @ApiOperation("获取Zuul网关的路由信息列表")
    @PostMapping("do-list")
    public Result<List<RouteZuulDto>> doList(ListSearchGatewayPo listSearchGatewayPo) {
        IPage<RouteZuulDto> page = routeZuulService.page(listSearchGatewayPo.getDescription(), listSearchGatewayPo.getPage(), listSearchGatewayPo.getLimit());
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @ApiOperation("获取Zuul网关正在工作的路由信息")
    @ApiImplicitParam(name = "gatewayName", value = "网关名称", required = true, dataType = "String")
    @PostMapping("do-list-working")
    public Result<List<RouteZuulVo>> doListWorking(@RequestParam(value = "gatewayName", required = false) String gatewayName) {
        if (StringUtils.isEmpty(gatewayName)) {
            return Result.ok();
        }

        List<RouteZuulVo> result = new ArrayList<>();
        List<ResultEntity> resultEntityList = routeResource.viewAllRoute(RouteZuulService.GATEWAY_TYPE, gatewayName);
        for (ResultEntity resultEntity : resultEntityList) {
            RouteZuulVo routeZuulVo = new RouteZuulVo();
            routeZuulVo.setHost(resultEntity.getHost());
            routeZuulVo.setPort(String.valueOf(resultEntity.getPort()));
            routeZuulVo.setRoutes(JsonUtil.fromJson(resultEntity.getResult(), new TypeReference<List<ZuulStrategyRouteEntity>>() {
            }));
            result.add(routeZuulVo);
        }
        return Result.ok(result);
    }

    @ApiOperation("获取所有Zuul网关的名称")
    @ApiImplicitParam(name = "gatewayName", value = "网关名称", required = true, dataType = "String")
    @PostMapping("do-list-gateway-names")
    public Result<List<String>> doListGatewayNames(@RequestParam(value = "gatewayName", required = false) String gatewayName) {
        return Result.ok(platformDiscoveryAdapter.getGatewayNames(RouteZuulService.GATEWAY_TYPE));
    }

    @ApiOperation("添加Zuul网关的路由")
    @PostMapping("do-insert")
    public Result<?> doInsert(RouteZuulDto routeZuulDto) {
        routeZuulService.insert(routeZuulDto);
        return Result.ok();
    }

    @ApiOperation("更新Zuul网关的路由")
    @PostMapping("do-update")
    public Result<?> doUpdate(RouteZuulDto routeZuulDto) {
        routeZuulService.update(routeZuulDto);
        return Result.ok();
    }

    @ApiOperation("启用Zuul网关的路由")
    @ApiImplicitParam(name = "id", value = "路由id", required = true, dataType = "String")
    @PostMapping("do-enable")
    public Result<?> doEnable(@RequestParam(value = "id") Long id) {
        routeZuulService.enable(id, true);
        return Result.ok();
    }

    @ApiOperation("禁用Zuul网关的路由")
    @ApiImplicitParam(name = "id", value = "路由id", required = true, dataType = "String")
    @PostMapping("do-disable")
    public Result<?> doDisable(@RequestParam(value = "id") Long id) {
        routeZuulService.enable(id, false);
        return Result.ok();
    }

    @ApiOperation("删除Zuul网关的路由")
    @ApiImplicitParam(name = "ids", value = "路由id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        routeZuulService.logicDelete(new HashSet<>(idList));
        return Result.ok();
    }

    @ApiOperation("发布Zuul网关的路由")
    @PostMapping("do-publish")
    public Result<?> doPublish() throws Exception {
        routeZuulService.publish();
        return Result.ok();
    }
}