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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.entity.GatewayType;
import com.nepxion.discovery.console.resource.ConfigResource;
import com.nepxion.discovery.console.resource.RouteResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.entity.po.RouteGatewayPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.GatewayRouteVo;
import com.nepxion.discovery.platform.server.event.PlatformPublisher;
import com.nepxion.discovery.platform.server.service.RouteGatewayService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(RouteGatewayController.PREFIX)
public class RouteGatewayController {
    public static final String PREFIX = "routegateway";

    private static final GatewayType GATEWAY_TYPE = GatewayType.SPRING_CLOUD_GATEWAY;

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private ConfigResource configResource;

    @Autowired
    private RouteResource routeResource;

    @Autowired
    private RouteGatewayService routeGatewayService;

    @Autowired
    private PlatformPublisher platformPublisher;

    // platformPublisher.asyncPublish(new PlatformAlarmEvent());

    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toworking")
    public String toWorking(final Model model) {
        model.addAttribute("gatewayNames", this.serviceResource.getGatewayList(GATEWAY_TYPE));
        return String.format("%s/%s", PREFIX, "working");
    }

    @GetMapping("toadd")
    public String toAdd(final Model model) {
        model.addAttribute("gatewayNames", this.serviceResource.getGatewayList(GATEWAY_TYPE));
        model.addAttribute("serviceNames", this.serviceResource.getServices());
        return String.format("%s/%s", PREFIX, "add");
    }

    @GetMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(name = "id") final Long id) {
        final RouteGatewayDto routeGateway = this.routeGatewayService.getById(id);
        routeGateway.setPredicates(CommonTool.formatTextarea(routeGateway.getPredicates()));
        routeGateway.setFilters(CommonTool.formatTextarea(routeGateway.getFilters()));
        routeGateway.setMetadata(CommonTool.formatTextarea(routeGateway.getMetadata()));

        model.addAttribute("gatewayNames", this.serviceResource.getGatewayList(GATEWAY_TYPE));
        model.addAttribute("serviceNames", this.serviceResource.getServices());
        model.addAttribute("route", routeGateway);
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<RouteGatewayDto>> list(@RequestParam(value = "page") final Integer pageNum,
                                              @RequestParam(value = "limit") final Integer pageSize,
                                              @RequestParam(value = "description", required = false) final String description) {
        final IPage<RouteGatewayDto> page = this.routeGatewayService.page(description, pageNum, pageSize);
        for (final RouteGatewayDto record : page.getRecords()) {
            record.setMetadata(record.getMetadata().replaceAll(PlatformConstant.ROW_SEPARATOR, ", "));
        }
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @PostMapping("listWorking")
    @ResponseBody
    public Result<List<GatewayRouteVo>> listWorking(@RequestParam(value = "gatewayName", required = false) final String gatewayName) {
        if (ObjectUtils.isEmpty(gatewayName)) {
            return Result.ok();
        }

        final List<GatewayRouteVo> result = new ArrayList<>();
        final List<ResultEntity> resultEntityList = this.routeResource.viewAllRoute(GATEWAY_TYPE, gatewayName);
        for (final ResultEntity resultEntity : resultEntityList) {
            final GatewayRouteVo gatewayRouteVo = new GatewayRouteVo();
            gatewayRouteVo.setHost(resultEntity.getHost());
            gatewayRouteVo.setPort(String.valueOf(resultEntity.getPort()));
            gatewayRouteVo.setRoutes(JsonUtil.fromJson(resultEntity.getResult(), new TypeReference<List<RouteGatewayPo>>() {
            }));
            result.add(gatewayRouteVo);
        }
        return Result.ok(result);
    }

    @PostMapping("listGatewayNames")
    @ResponseBody
    public Result<List<String>> listGatewayNames(@RequestParam(value = "gatewayName", required = false) final String gatewayName) {
        return Result.ok(this.serviceResource.getGatewayList(GATEWAY_TYPE));
    }

    @PostMapping("add")
    @ResponseBody
    public Result<?> add(RouteGatewayDto routeGateway) {
        this.routeGatewayService.insert(routeGateway);
        return Result.ok();
    }

    @PostMapping("edit")
    @ResponseBody
    public Result<?> edit(RouteGatewayDto routeGateway) {
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
        final List<RouteGatewayDto> routeGatewayList = this.routeGatewayService.listEnabled();
        final Map<String, List<RouteGatewayPo>> gatewayNameRouteGatewayPoMap = new HashMap<>();

        for (final RouteGatewayDto routeGateway : routeGatewayList) {
            final RouteGatewayPo routeGatewayPo = new RouteGatewayPo();
            routeGatewayPo.setId(routeGateway.getRouteId());
            routeGatewayPo.setUri(routeGateway.getUri());
            routeGatewayPo.setPredicates(Arrays.asList(routeGateway.getPredicates().split(PlatformConstant.ROW_SEPARATOR)));
            routeGatewayPo.setFilters(Arrays.asList(routeGateway.getFilters().split(PlatformConstant.ROW_SEPARATOR)));
            routeGatewayPo.setOrder(routeGateway.getOrder());
            routeGatewayPo.setMetadata(CommonTool.asMap(routeGateway.getMetadata(), PlatformConstant.ROW_SEPARATOR));

            if (gatewayNameRouteGatewayPoMap.containsKey(routeGateway.getGatewayName())) {
                gatewayNameRouteGatewayPoMap.get(routeGateway.getGatewayName()).add(routeGatewayPo);
            } else {
                final List<RouteGatewayPo> routeGatewayPoList = new ArrayList<>();
                routeGatewayPoList.add(routeGatewayPo);
                gatewayNameRouteGatewayPoMap.put(routeGateway.getGatewayName(), routeGatewayPoList);
            }
        }

        for (Map.Entry<String, List<RouteGatewayPo>> pair : gatewayNameRouteGatewayPoMap.entrySet()) {
            final String gatewayName = pair.getKey();
            final String group = this.serviceResource.getGroup(gatewayName);
            final String serviceId = gatewayName.concat("-").concat(PlatformConstant.GATEWAY_DYNAMIC_ROUTE);
            final String config = JsonUtil.toPrettyJson(pair.getValue());
            this.configResource.updateRemoteConfig(group, serviceId, config);
        }

        return Result.ok();
    }
}