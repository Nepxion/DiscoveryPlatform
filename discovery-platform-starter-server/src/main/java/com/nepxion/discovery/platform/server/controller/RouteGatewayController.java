package com.nepxion.discovery.platform.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.entity.GatewayType;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.RouteResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.RouteGateway;
import com.nepxion.discovery.platform.server.entity.vo.GatewayStrategyRouteEntity;
import com.nepxion.discovery.platform.server.interfaces.RouteGatewayService;
import com.nepxion.discovery.platform.server.tool.common.CommonTool;
import com.nepxion.discovery.platform.server.tool.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(RouteGatewayController.PREFIX)
public class RouteGatewayController {
    public static final String PREFIX = "routegateway";
    @Autowired
    private ServiceResource serviceResource;
    @Autowired
    private ConfigResource configResource;
    @Autowired
    private RouteResource routeResource;
    @Autowired
    private RouteGatewayService routeGatewayService;
    private static final GatewayType GATEWAY_TYPE = GatewayType.SPRING_CLOUD_GATEWAY;

    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toworking")
    public String toWorking(final Model model) {
        final List<String> gatewayNames = serviceResource.getGatewayList(GATEWAY_TYPE);
        model.addAttribute("gatewayNames", gatewayNames);
        return String.format("%s/%s", PREFIX, "working");
    }

    @GetMapping("toadd")
    public String toAdd(final Model model) {
        model.addAttribute("serviceNames", this.serviceResource.getServices());
        return String.format("%s/%s", PREFIX, "add");
    }

    @GetMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(name = "id") final Long id) {
        final List<String> serviceNameList = this.serviceResource.getServices();
        final RouteGateway routeGateway = this.routeGatewayService.getById(id);
        routeGateway.setPredicates(CommonTool.formatTextarea(routeGateway.getPredicates()));
        routeGateway.setFilters(CommonTool.formatTextarea(routeGateway.getFilters()));
        routeGateway.setMetadata(CommonTool.formatTextarea(routeGateway.getMetadata()));
        model.addAttribute("serviceNames", serviceNameList);
        model.addAttribute("route", routeGateway);
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<RouteGateway>> list(@RequestParam(value = "page") final Integer pageNum,
                                           @RequestParam(value = "limit") final Integer pageSize,
                                           @RequestParam(value = "description", required = false) final String description) {
        final IPage<RouteGateway> page = this.routeGatewayService.page(description, pageNum, pageSize);
        for (final RouteGateway record : page.getRecords()) {
            record.setMetadata(record.getMetadata().replaceAll(PlatformConstant.ROW_SEPARATOR, ", "));
        }
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @PostMapping("listWorking")
    @ResponseBody
    public Result<List<String>> listWorking(@RequestParam(value = "page") final Integer pageNum,
                                            @RequestParam(value = "limit") final Integer pageSize,
                                            @RequestParam(value = "routeName", required = false) final String routeName) {
        final List<String> gatewayNames = serviceResource.getGatewayList(GATEWAY_TYPE);
        for (final String gatewayName : gatewayNames) {
            List<ResultEntity> resultEntityList = this.routeResource.viewAllRoute(GATEWAY_TYPE, gatewayName);
            System.out.println(resultEntityList);
        }
        return Result.ok();
//        final List<String> workingRouteList = this.gatewayRpcService.listWorkingRoutes();
//        if (null == workingRouteList || workingRouteList.isEmpty()) {
//            return Result.ok();
//        }
//        final List<WorkingVo> workingVoList = new ArrayList<>(workingRouteList.size());
//        for (final String workingRoute : workingRouteList) {
//            final WorkingVo workingVo = new WorkingVo();
//            workingVo.setData(workingRoute
//                    .replaceAll("RouteDefinition", "")
//                    .replaceAll("PredicateDefinition", "")
//                    .replaceAll("FilterDefinition", ""));
//            workingVoList.add(workingVo);
//        }
//        return Result.ok(workingVoList);
    }

    @PostMapping("add")
    @ResponseBody
    public Result<?> add(RouteGateway routeGateway) {
        this.routeGatewayService.insert(routeGateway);
        return Result.ok();
    }

    @PostMapping("edit")
    @ResponseBody
    public Result<?> edit(RouteGateway routeGateway) {
        this.routeGatewayService.update(routeGateway);
        return Result.ok();
    }

    @PostMapping("del")
    @ResponseBody
    public Result<?> del(@RequestParam(value = "ids") final String ids) {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.routeGatewayService.delete(new HashSet<>(idList));
        return Result.ok();
    }

    @PostMapping("enable")
    @ResponseBody
    public Result<?> enable(@RequestParam(value = "id") final Long id) {
        this.routeGatewayService.enable(id, true);
        return Result.ok();
    }

    @PostMapping("disable")
    @ResponseBody
    public Result<?> disable(@RequestParam(value = "id") final Long id) {
        this.routeGatewayService.enable(id, false);
        return Result.ok();
    }

    @PostMapping("publish")
    @ResponseBody
    public Result<?> publish() throws Exception {
        final List<String> gatewayNames = this.serviceResource.getGateways();
        final List<RouteGateway> routeGatewayList = this.routeGatewayService.listEnabled();
        final List<GatewayStrategyRouteEntity> gatewayStrategyRouteEntityList = new ArrayList<>(routeGatewayList.size());

        for (final RouteGateway routeGateway : routeGatewayList) {
            final GatewayStrategyRouteEntity gatewayStrategyRouteEntity = new GatewayStrategyRouteEntity();
            gatewayStrategyRouteEntity.setId(routeGateway.getRouteId());
            gatewayStrategyRouteEntity.setUri(routeGateway.getUri());
            gatewayStrategyRouteEntity.setPredicates(Arrays.asList(routeGateway.getPredicates().split(PlatformConstant.ROW_SEPARATOR)));
            gatewayStrategyRouteEntity.setFilters(Arrays.asList(routeGateway.getFilters().split(PlatformConstant.ROW_SEPARATOR)));
            gatewayStrategyRouteEntity.setOrder(routeGateway.getOrder());
            gatewayStrategyRouteEntity.setMetadata(CommonTool.asMap(routeGateway.getMetadata(), PlatformConstant.ROW_SEPARATOR));
            gatewayStrategyRouteEntityList.add(gatewayStrategyRouteEntity);
        }

        for (final String gatewayName : gatewayNames) {
            final String group = this.serviceResource.getGroup(gatewayName);
            final String serviceId = gatewayName.concat("-").concat(PlatformConstant.GATEWAY_DYNAMIC_ROUTE);
            final String config = JsonUtil.toJson(gatewayStrategyRouteEntityList);
            this.configResource.updateRemoteConfig(group, serviceId, CommonTool.prettyFormat(config));
        }

        return Result.ok();
    }
}