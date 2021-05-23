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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.resource.RouteResource;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.entity.po.RouteZuulPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.GatewayZuulVo;
import com.nepxion.discovery.platform.server.service.RouteZuulService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(RouteZuulController.PREFIX)
public class RouteZuulController {
    public static final String PREFIX = "routezuul";

    @Autowired
    private ServiceResource serviceResource;

    @Autowired
    private RouteResource routeResource;

    @Autowired
    private RouteZuulService routeZuulService;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("working")
    public String working(final Model model) {
        model.addAttribute("gatewayNames", this.serviceResource.getGatewayList(RouteZuulService.GATEWAY_TYPE));
        return String.format("%s/%s", PREFIX, "working");
    }

    @GetMapping("add")
    public String add(final Model model) {
        model.addAttribute("gatewayNames", this.serviceResource.getGatewayList(RouteZuulService.GATEWAY_TYPE));
        model.addAttribute("serviceNames", this.serviceResource.getServices());
        return String.format("%s/%s", PREFIX, "add");
    }

    @GetMapping("edit")
    public String edit(final Model model,
                       @RequestParam(name = "id") final Long id) {
        model.addAttribute("gatewayNames", this.serviceResource.getGatewayList(RouteZuulService.GATEWAY_TYPE));
        model.addAttribute("serviceNames", this.serviceResource.getServices());
        model.addAttribute("route", this.routeZuulService.getById(id));
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("do-list")
    @ResponseBody
    public Result<List<RouteZuulDto>> doList(@RequestParam(value = "page") final Integer pageNum,
                                             @RequestParam(value = "limit") final Integer pageSize,
                                             @RequestParam(value = "description", required = false) final String description) {
        final IPage<RouteZuulDto> page = this.routeZuulService.page(description, pageNum, pageSize);
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @PostMapping("do-list-working")
    @ResponseBody
    public Result<List<GatewayZuulVo>> doListWorking(@RequestParam(value = "gatewayName", required = false) final String gatewayName) {
        if (ObjectUtils.isEmpty(gatewayName)) {
            return Result.ok();
        }

        final List<GatewayZuulVo> result = new ArrayList<>();
        final List<ResultEntity> resultEntityList = this.routeResource.viewAllRoute(RouteZuulService.GATEWAY_TYPE, gatewayName);
        for (final ResultEntity resultEntity : resultEntityList) {
            final GatewayZuulVo gatewayZuulVo = new GatewayZuulVo();
            gatewayZuulVo.setHost(resultEntity.getHost());
            gatewayZuulVo.setPort(String.valueOf(resultEntity.getPort()));
            gatewayZuulVo.setRoutes(JsonUtil.fromJson(resultEntity.getResult(), new TypeReference<List<RouteZuulPo>>() {
            }));
            result.add(gatewayZuulVo);
        }
        return Result.ok(result);
    }

    @PostMapping("do-list-gateway-names")
    @ResponseBody
    public Result<List<String>> doListGatewayNames(@RequestParam(value = "gatewayName", required = false) final String gatewayName) {
        return Result.ok(this.serviceResource.getGatewayList(RouteZuulService.GATEWAY_TYPE));
    }

    @PostMapping("do-add")
    @ResponseBody
    public Result<?> doAdd(RouteZuulDto routeZuulDto) {
        this.routeZuulService.insert(routeZuulDto);
        return Result.ok();
    }

    @PostMapping("do-edit")
    @ResponseBody
    public Result<?> doEdit(RouteZuulDto routeZuulDto) {
        this.routeZuulService.update(routeZuulDto);
        return Result.ok();
    }

    @PostMapping("do-enable")
    @ResponseBody
    public Result<?> doEnable(@RequestParam(value = "id") final Long id) {
        this.routeZuulService.enable(id, true);
        return Result.ok();
    }

    @PostMapping("do-disable")
    @ResponseBody
    public Result<?> doDisable(@RequestParam(value = "id") final Long id) {
        this.routeZuulService.enable(id, false);
        return Result.ok();
    }

    @PostMapping("do-delete")
    @ResponseBody
    public Result<?> doDelete(@RequestParam(value = "ids") final String ids) {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.routeZuulService.logicDelete(new HashSet<>(idList));
        return Result.ok();
    }

    @PostMapping("do-publish")
    @ResponseBody
    public Result<?> doPublish() throws Exception {
        this.routeZuulService.publish();
        return Result.ok();
    }

}