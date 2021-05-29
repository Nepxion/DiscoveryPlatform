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

import com.nepxion.discovery.platform.server.service.MenuService;
import com.nepxion.discovery.platform.server.service.RoleService;

@Controller
@RequestMapping(PermissionController.PREFIX)
public class PermissionPageController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @GetMapping("list")
    public String list(Model model) throws Exception {
        model.addAttribute("roles", roleService.getNotSuperAdmin());
        model.addAttribute("menus", menuService.listNotEmptyUrlMenus());
        return String.format("%s/%s", PermissionController.PREFIX, "list");
    }

    @GetMapping("add")
    public String add(Model model) throws Exception {
        model.addAttribute("roles", roleService.getNotSuperAdmin());
        return String.format("%s/%s", PermissionController.PREFIX, "add");
    }
}