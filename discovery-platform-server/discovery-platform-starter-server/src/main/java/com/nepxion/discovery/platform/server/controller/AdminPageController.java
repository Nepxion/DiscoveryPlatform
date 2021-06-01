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

import com.nepxion.discovery.platform.server.adapter.PlatformLoginAdapter;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.exception.PlatformException;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.RoleService;

@Controller
@RequestMapping(AdminController.PREFIX)
public class AdminPageController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PlatformLoginAdapter loginAdapter;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", AdminController.PREFIX, "list");
    }

    @GetMapping("add")
    public String add(Model model) throws Exception {
        model.addAttribute("roles", roleService.listOrderByName());

        if (loginAdapter.getLoginMode() == LoginMode.DATABASE) {
            return String.format("%s/%s", AdminController.PREFIX, "add");
        }
        if (loginAdapter.getLoginMode() == LoginMode.LDAP) {
            return String.format("%s/%s", AdminController.PREFIX, "addldap");
        }
        throw new PlatformException(String.format("暂不支持登录模式[%s]", loginAdapter.getLoginMode()));
    }

    @GetMapping("edit")
    public String edit(Model model, @RequestParam(name = "id") Long id) throws Exception {
        model.addAttribute("admin", adminService.getById(id));
        model.addAttribute("roles", roleService.listOrderByName());
        model.addAttribute("loginMode", loginAdapter.getLoginMode());
        return String.format("%s/%s", AdminController.PREFIX, "edit");
    }
}