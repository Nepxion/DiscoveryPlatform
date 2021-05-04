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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.configuration.properties.PlatformProperties;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.enums.Mode;
import com.nepxion.discovery.platform.server.ineterfaces.AdminService;
import com.nepxion.discovery.platform.server.ineterfaces.RoleService;
import com.nepxion.discovery.platform.tool.common.CommonTool;
import com.nepxion.discovery.platform.tool.exception.BusinessException;
import com.nepxion.discovery.platform.tool.web.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(AdminController.PREFIX)
public class AdminController {
    public static final String PREFIX = "admin";

    private final PlatformProperties platformProperties;
    private final AdminService adminService;
    private final RoleService roleService;

    public AdminController(final PlatformProperties platformProperties,
                           final AdminService adminService,
                           final RoleService roleService) {
        this.platformProperties = platformProperties;
        this.adminService = adminService;
        this.roleService = roleService;
    }


    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toadd")
    public String toAdd(final Model model) throws Exception {
        model.addAttribute("roles", this.roleService.listOrderByName());

        if (this.platformProperties.getMode() == Mode.DB ||
                this.platformProperties.getMode() == Mode.MEMORY) {
            return String.format("%s/%s", PREFIX, "add");
        }
        if (this.platformProperties.getMode() == Mode.LDAP) {
            return String.format("%s/%s", PREFIX, "addldap");
        }
        throw new BusinessException(String.format("暂不支持登录模式[%s]", this.platformProperties.getMode()));
    }

    @GetMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(name = "id") final Long id) throws Exception {
        model.addAttribute("admin", this.adminService.getById(id));
        model.addAttribute("roles", this.roleService.listOrderByName());
        model.addAttribute("loginMode", this.platformProperties.getMode());
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<Admin>> list(@RequestParam(value = "name", required = false) final String name,
                                    @RequestParam(value = "page") final Integer pageNum,
                                    @RequestParam(value = "limit") final Integer pageSize) throws Exception {
        final IPage<Admin> adminPage = this.adminService.list(this.platformProperties.getMode(), name, pageNum, pageSize);
        return Result.ok(adminPage.getRecords(), adminPage.getTotal());
    }

    @PostMapping("repwd")
    @ResponseBody
    public Result<?> repwd(@RequestParam(value = "id") final Long id) throws Exception {
        if (this.platformProperties.getMode() == Mode.DB ||
                this.platformProperties.getMode() == Mode.MEMORY) {
            if (this.adminService.resetPassword(id)) {
                return Result.ok();
            } else {
                return Result.error("密码修改失败");
            }
        } else {
            return Result.error("登陆模式为数据库才能修改密码");
        }
    }

    @PostMapping("add")
    @ResponseBody
    public Result<?> add(@RequestParam(value = "roleId") final Long roleId,
                         @RequestParam(value = "username") final String username,
                         @RequestParam(value = "password", defaultValue = "") final String password,
                         @RequestParam(value = "name") final String name,
                         @RequestParam(value = "phoneNumber") final String phoneNumber,
                         @RequestParam(value = "email") final String email,
                         @RequestParam(value = "remark") final String remark) throws Exception {
        this.adminService.insert(this.platformProperties.getMode(), roleId, username, password, name, phoneNumber, email, remark);
        return Result.ok();
    }

    @PostMapping("edit")
    @ResponseBody
    public Result<?> edit(@RequestParam(value = "id") final Long id,
                          @RequestParam(value = "roleId") final Long roleId,
                          @RequestParam(value = "username") final String username,
                          @RequestParam(value = "name") final String name,
                          @RequestParam(value = "phoneNumber") final String phoneNumber,
                          @RequestParam(value = "email") final String email,
                          @RequestParam(value = "remark") final String remark) throws Exception {
        this.adminService.update(id, roleId, username, name, phoneNumber, email, remark);
        return Result.ok();
    }

    @PostMapping("del")
    @ResponseBody
    public Result<?> del(@RequestParam(value = "ids") final String ids) throws Exception {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.adminService.removeById(idList);
        return Result.ok();
    }

    @PostMapping("search")
    @ResponseBody
    public Result<List<Admin>> search(@RequestParam(value = "keyword", defaultValue = "") final String keyword) throws Exception {
        if (ObjectUtils.isEmpty(keyword.trim())) {
            return Result.ok();
        }
        return Result.ok(this.adminService.search(keyword, 0, 10));
    }
}