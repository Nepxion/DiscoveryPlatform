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
import com.nepxion.discovery.common.entity.GatewayStrategyRouteEntity;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.RouteResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchGatewayPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.RouteGatewayVo;
import com.nepxion.discovery.platform.server.service.RouteGatewayService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("Spring Cloud Gateway动态路由接口")
@RestController
@RequestMapping(RouteGatewayController.PREFIX)
public class RouteGatewayController {
    public static final String PREFIX = "route-gateway";

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private RouteResource routeResource;

    @Autowired
    private RouteGatewayService routeGatewayService;

    @ApiOperation(value = "获取Spring Cloud Gateway网关的路由信息列表")
    @PostMapping("do-list")
    public Result<List<RouteGatewayDto>> doList(ListSearchGatewayPo listSearchGatewayPo) {
        IPage<RouteGatewayDto> page = routeGatewayService.page(listSearchGatewayPo.getDescription(), listSearchGatewayPo.getPage(), listSearchGatewayPo.getLimit());
        for (RouteGatewayDto record : page.getRecords()) {
            record.setMetadata(record.getMetadata().replaceAll(PlatformConstant.ROW_SEPARATOR, ", "));
        }
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @ApiOperation(value = "获取Spring Cloud Gateway网关正在工作的路由信息")
    @ApiImplicitParam(name = "gatewayName", value = "网关名称", required = true, dataType = "String")
    @PostMapping("do-list-working")
    public Result<List<RouteGatewayVo>> doListWorking(@RequestParam(value = "gatewayName", required = true, defaultValue = StringUtils.EMPTY) String gatewayName) {
        if (StringUtils.isEmpty(gatewayName)) {
            return Result.ok();
        }

        List<RouteGatewayVo> result = new ArrayList<>();
        List<ResultEntity> resultEntityList = routeResource.viewAllRoute(RouteGatewayService.GATEWAY_TYPE, gatewayName);
        for (ResultEntity resultEntity : resultEntityList) {
            RouteGatewayVo routeGatewayVo = new RouteGatewayVo();
            routeGatewayVo.setHost(resultEntity.getHost());
            routeGatewayVo.setPort(String.valueOf(resultEntity.getPort()));
            routeGatewayVo.setRoutes(JsonUtil.fromJson(resultEntity.getResult(), new TypeReference<List<GatewayStrategyRouteEntity>>() {
            }));
            result.add(routeGatewayVo);
        }
        return Result.ok(result);
    }

    @ApiOperation(value = "获取所有Spring Cloud Gateway网关的名称")
    @ApiImplicitParam(name = "gatewayName", value = "网关名称", required = true, dataType = "String")
    @PostMapping("do-list-gateway-names")
    public Result<List<String>> doListGatewayNames(@RequestParam(value = "gatewayName", required = false) String gatewayName) {
        return Result.ok(serviceResource.getGatewayList(RouteGatewayService.GATEWAY_TYPE));
    }

    @ApiOperation(value = "添加Spring Cloud Gateway网关的路由")
    @PostMapping("do-insert")
    public Result<?> doInsert(RouteGatewayDto routeGateway) {
        routeGatewayService.insert(routeGateway);
        return Result.ok();
    }

    @ApiOperation(value = "更新Spring Cloud Gateway网关的路由")
    @PostMapping("do-update")
    public Result<?> doUpdate(RouteGatewayDto routeGateway) {
        routeGatewayService.update(routeGateway);
        return Result.ok();
    }

    @ApiOperation(value = "启用Spring Cloud Gateway网关的路由")
    @ApiImplicitParam(name = "id", value = "路由id", required = true, dataType = "String")
    @PostMapping("do-enable")
    public Result<?> doEnable(@RequestParam(value = "id") Long id) {
        routeGatewayService.enable(id, true);
        return Result.ok();
    }

    @ApiOperation(value = "禁用Spring Cloud Gateway网关的路由")
    @ApiImplicitParam(name = "id", value = "路由id", required = true, dataType = "String")
    @PostMapping("do-disable")
    public Result<?> doDisable(@RequestParam(value = "id") Long id) {
        routeGatewayService.enable(id, false);
        return Result.ok();
    }

    @ApiOperation(value = "删除Spring Cloud Gateway网关的路由")
    @ApiImplicitParam(name = "ids", value = "路由id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        routeGatewayService.logicDelete(new HashSet<>(idList));
        return Result.ok();
    }

    @ApiOperation(value = "发布Spring Cloud Gateway网关的路由")
    @PostMapping("do-publish")
    public Result<?> doPublish() throws Exception {
        routeGatewayService.publish();
        return Result.ok();
    }
}