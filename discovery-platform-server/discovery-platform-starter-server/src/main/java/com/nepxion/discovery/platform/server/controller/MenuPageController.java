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

import com.nepxion.discovery.platform.server.service.MenuService;

@Controller
@RequestMapping(MenuController.PREFIX)
public class MenuPageController {
    @Autowired
    private MenuService menuService;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", MenuController.PREFIX, "list");
    }

    @RequestMapping("add")
    public String add(Model model) throws Exception {
        model.addAttribute("menus", menuService.listEmptyUrlMenus());
        return String.format("%s/%s", MenuController.PREFIX, "add");
    }

    @RequestMapping("edit")
    public String edit(Model model, @RequestParam(value = "id") Long id) throws Exception {
        model.addAttribute("menu", menuService.getById(id));
        model.addAttribute("menus", menuService.listEmptyUrlMenus());
        return String.format("%s/%s", MenuController.PREFIX, "edit");
    }
}