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

import com.nepxion.discovery.platform.server.service.RoleService;

@Controller
@RequestMapping(RoleController.PREFIX)
public class RolePageController {
    @Autowired
    private RoleService roleService;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", RoleController.PREFIX, "list");
    }

    @GetMapping("add")
    public String add() {
        return String.format("%s/%s", RoleController.PREFIX, "add");
    }

    @GetMapping("edit")
    public String edit(Model model, @RequestParam(name = "id") Long id) {
        model.addAttribute("role", roleService.getById(id));
        return String.format("%s/%s", RoleController.PREFIX, "edit");
    }
}