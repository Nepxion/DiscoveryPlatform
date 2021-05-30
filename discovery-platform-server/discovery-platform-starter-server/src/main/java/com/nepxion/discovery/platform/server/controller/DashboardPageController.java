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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

@Api("首页Dashboard看板相关接口")
@Controller
@RequestMapping(DashboardController.PREFIX)
public class DashboardPageController {
    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", DashboardController.PREFIX, "list");
    }
}