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

import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;

@Controller
@RequestMapping(BlacklistController.PREFIX)
public class BlacklistPageController {
    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", BlacklistController.PREFIX, "list");
    }

    @GetMapping("add")
    public String add(Model model) {
        model.addAttribute("gatewayNames", platformDiscoveryAdapter.getGatewayNames());
        model.addAttribute("serviceNames", platformDiscoveryAdapter.getServiceNames());
        return String.format("%s/%s", BlacklistController.PREFIX, "add");
    }
}