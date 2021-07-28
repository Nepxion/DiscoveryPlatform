package com.nepxion.discovery.platform.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: discovery-platform
 * @description:
 * @author: xiaolong
 * @create: 2021-07-17 09:41
 **/

@Controller
@RequestMapping(InstanceInfoController.PREFIX)
public class InstanceInfoPageController {

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", InstanceInfoController.PREFIX, "list");
    }

    @GetMapping("detail")
    public String add(Model model, String obj)  {
        return String.format("%s/%s", InstanceInfoController.PREFIX, "detail");
    }
}
