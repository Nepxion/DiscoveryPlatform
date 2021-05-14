package com.nepxion.discovery.platform.starter.server.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.RouteResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.starter.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.starter.server.entity.dto.RouteGateway;
import com.nepxion.discovery.platform.starter.server.interfaces.RouteGatewayService;
import com.nepxion.discovery.platform.starter.server.tool.common.CommonTool;
import com.nepxion.discovery.platform.starter.server.tool.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toworking")
    public String toWorking() {
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
        model.addAttribute("serviceNames", this.serviceResource.getServices());
        model.addAttribute("route", this.routeGatewayService.getById(id));
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<RouteGateway>> list(@RequestParam(value = "page") final Integer pageNum,
                                           @RequestParam(value = "limit") final Integer pageSize,
                                           @RequestParam(value = "description", required = false) final String description) {
        final IPage<RouteGateway> page = this.routeGatewayService.page(description, pageNum, pageSize);
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @PostMapping("listWorking")
    @ResponseBody
    public Result<List<String>> listWorking(@RequestParam(value = "page") final Integer pageNum,
                                            @RequestParam(value = "limit") final Integer pageSize,
                                            @RequestParam(value = "routeName", required = false) final String routeName) throws Exception {
        List<String> gatewayNames = this.serviceResource.getGateways();

        for (final String gatewayName : gatewayNames) {
            List<ResultEntity> resultEntityList = this.routeResource.viewAllRoute("gateway", gatewayName);
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

        for (final String gatewayName : gatewayNames) {
            final List<ServiceInstance> instances = this.serviceResource.getInstances(gatewayName);

            final Set<String> groupSet = new HashSet<>();
            for (final ServiceInstance instance : instances) {
                final String group = instance.getMetadata().get(DiscoveryConstant.GROUP);
                if (ObjectUtils.isEmpty(group)) {
                    continue;
                }
                groupSet.add(group);
            }

            for (final String group : groupSet) {
                configResource.updateRemoteConfig(
                        group,
                        gatewayName.concat("-").concat(PlatformConstant.GATEWAY_DYNAMIC_ROUTE),
                        JsonUtil.toJson(routeGatewayList)
                );
            }
        }

        // 不通过网关， 直接通过轮询调用网关的方式
        // this.routeResource.updateAllRoute(
        //         "gateway",
        //         gatewayNames.get(0),
        //         JsonUtil.toJson(routeGatewayList));

        return Result.ok();
    }
}