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
import com.nepxion.discovery.platform.server.configuration.properties.PlatformServerProperties;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.tool.common.CommonTool;
import com.nepxion.discovery.platform.server.common.Tool;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.interfaces.AdminService;
import com.nepxion.discovery.platform.server.interfaces.RoleService;
import com.nepxion.discovery.platform.server.tool.exception.BusinessException;
import com.nepxion.discovery.platform.server.tool.web.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(AdminController.PREFIX)
public class AdminController {
    public static final String PREFIX = "admin";

    private final PlatformServerProperties platformProperties;
    private final AdminService adminService;
    private final RoleService roleService;

    public AdminController(final PlatformServerProperties platformProperties,
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

        if (this.platformProperties.getLoginMode() == LoginMode.DB) {
            return String.format("%s/%s", PREFIX, "add");
        }
        if (this.platformProperties.getLoginMode() == LoginMode.LDAP) {
            return String.format("%s/%s", PREFIX, "addldap");
        }
        throw new BusinessException(String.format("暂不支持登录模式[%s]", this.platformProperties.getLoginMode()));
    }

    @GetMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(name = "id") final Long id) throws Exception {
        model.addAttribute("admin", this.adminService.getById(id));
        model.addAttribute("roles", this.roleService.listOrderByName());
        model.addAttribute("loginMode", this.platformProperties.getLoginMode());
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<Admin>> list(@RequestParam(value = "name", required = false) final String name,
                                    @RequestParam(value = "page") final Integer pageNum,
                                    @RequestParam(value = "limit") final Integer pageSize) throws Exception {
        final IPage<Admin> adminPage = this.adminService.list(this.platformProperties.getLoginMode(), name, pageNum, pageSize);
        return Result.ok(adminPage.getRecords(), adminPage.getTotal());
    }

    @PostMapping("repwd")
    @ResponseBody
    public Result<?> repwd(@RequestParam(value = "id") final Long id) throws Exception {
        final SysAdmin sysAdmin = this.adminService.getById(id);
        if (null == sysAdmin) {
            return Result.error(String.format("用户[id=%s]不存在", id));
        }
        if (this.adminService.changePassword(id,
                sysAdmin.getPassword(),
                Tool.hash(PlatformConstant.DEFAULT_ADMIN_PASSWORD))) {
            return Result.ok();
        } else {
            return Result.error("密码修改失败");
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
        this.adminService.insert(this.platformProperties.getLoginMode(), roleId, username, password, name, phoneNumber, email, remark);
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
        this.adminService.removeByIds(new HashSet<>(idList));
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