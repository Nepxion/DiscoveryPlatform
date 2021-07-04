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

import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.service.RouteZuulService;

@Controller
@RequestMapping(RouteZuulController.PREFIX)
public class RouteZuulPageController {

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Autowired
    private RouteZuulService routeZuulService;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", RouteZuulController.PREFIX, "list");
    }

    @GetMapping("working")
    public String working(Model model) {
        model.addAttribute("gatewayNames", platformDiscoveryAdapter.getGatewayNames(RouteZuulService.GATEWAY_TYPE));
        return String.format("%s/%s", RouteZuulController.PREFIX, "working");
    }

    @GetMapping("add")
    public String add(Model model) {
        return String.format("%s/%s", RouteZuulController.PREFIX, "add");
    }

    @GetMapping("edit")
    public String edit(Model model, @RequestParam(name = "id") Long id) {
        model.addAttribute("route", routeZuulService.getById(id));
        return String.format("%s/%s", RouteZuulController.PREFIX, "edit");
    }
}