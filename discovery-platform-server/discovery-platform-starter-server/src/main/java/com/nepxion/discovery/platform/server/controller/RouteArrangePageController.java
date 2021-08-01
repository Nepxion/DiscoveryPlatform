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

import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.service.RouteArrangeService;

@Controller
@RequestMapping(RouteArrangeController.PREFIX)
public class RouteArrangePageController {
    @Autowired
    private RouteArrangeService routeArrangeService;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", RouteArrangeController.PREFIX, "list");
    }

    @GetMapping("add")
    public String add(Model model) throws Exception {
        RouteArrangeDto routeArrangeDto = new RouteArrangeDto();
        routeArrangeDto.setStrategyType(RouteArrangeDto.StrategyType.VERSION.getCode());
        model.addAttribute("entity", routeArrangeDto);
        return String.format("%s/%s", RouteArrangeController.PREFIX, "add");
    }

    @GetMapping("edit")
    public String edit(Model model, @RequestParam(name = "id") Long id) {
        RouteArrangeDto routeArrangeDto = routeArrangeService.getById(id);
        model.addAttribute("entity", routeArrangeDto);
        return String.format("%s/%s", RouteArrangeController.PREFIX, "edit");
    }
}