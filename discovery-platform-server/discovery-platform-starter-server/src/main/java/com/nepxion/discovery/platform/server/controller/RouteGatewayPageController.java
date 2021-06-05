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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.service.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.service.RouteGatewayService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

@Controller
@RequestMapping(RouteGatewayController.PREFIX)
public class RouteGatewayPageController {

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Autowired
    private RouteGatewayService routeGatewayService;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", RouteGatewayController.PREFIX, "list");
    }

    @GetMapping("working")
    public String working(Model model) {
        model.addAttribute("gatewayNames", platformDiscoveryAdapter.getGatewayNames(RouteGatewayService.GATEWAY_TYPE));
        return String.format("%s/%s", RouteGatewayController.PREFIX, "working");
    }

    @GetMapping("add")
    public String add(Model model) {
        model.addAttribute("gatewayNames", platformDiscoveryAdapter.getGatewayNames(RouteGatewayService.GATEWAY_TYPE));
        model.addAttribute("serviceNames", platformDiscoveryAdapter.getServiceNames());
        return String.format("%s/%s", RouteGatewayController.PREFIX, "add");
    }

    @GetMapping("edit")
    public String edit(Model model, @RequestParam(name = "id") Long id) {
        RouteGatewayDto routeGateway = routeGatewayService.getById(id);
        routeGateway.setPredicates(CommonTool.formatTextarea(routeGateway.getPredicates()));
        routeGateway.setUserPredicates(CommonTool.formatTextarea(routeGateway.getUserPredicates()));
        routeGateway.setFilters(CommonTool.formatTextarea(routeGateway.getFilters()));
        routeGateway.setUserFilters(CommonTool.formatTextarea(routeGateway.getUserFilters()));
        routeGateway.setMetadata(CommonTool.formatTextarea(routeGateway.getMetadata()));
        model.addAttribute("gatewayNames", platformDiscoveryAdapter.getGatewayNames(RouteGatewayService.GATEWAY_TYPE));
        model.addAttribute("serviceNames", platformDiscoveryAdapter.getServiceNames());
        model.addAttribute("route", routeGateway);
        return String.format("%s/%s", RouteGatewayController.PREFIX, "edit");
    }
}