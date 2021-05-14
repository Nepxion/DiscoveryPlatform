package com.nepxion.discovery.platform.starter.server.controller;

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
import com.nepxion.discovery.platform.starter.server.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.starter.server.entity.dto.SysRole;
import com.nepxion.discovery.platform.starter.server.interfaces.AdminService;
import com.nepxion.discovery.platform.starter.server.interfaces.RoleService;
import com.nepxion.discovery.platform.starter.server.tool.common.CommonTool;
import com.nepxion.discovery.platform.starter.server.tool.web.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(RoleController.PREFIX)
public class RoleController {
    public static final String PREFIX = "role";

    private final AdminService adminService;
    private final RoleService roleService;

    public RoleController(final AdminService adminService,
                          final RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toadd")
    public String toAdd() {
        return String.format("%s/%s", PREFIX, "add");
    }

    @GetMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(name = "id") final Long id) throws Exception {
        model.addAttribute("role", this.roleService.getById(id));
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<SysRole>> list(@RequestParam(value = "name", required = false) final String name,
                                      @RequestParam(value = "page") final Integer pageNum,
                                      @RequestParam(value = "limit") final Integer pageSize) throws Exception {
        final IPage<SysRole> sysAdmins = this.roleService.list(name, pageNum, pageSize);
        return Result.ok(sysAdmins.getRecords(), sysAdmins.getTotal());
    }

    @PostMapping("add")
    @ResponseBody
    public Result<?> add(@RequestParam(value = "name") final String name,
                         @RequestParam(value = "superAdmin") final boolean superAdmin,
                         @RequestParam(value = "remark") final String remark) throws Exception {
        this.roleService.insert(name, superAdmin, remark);
        return Result.ok();
    }

    @PostMapping("edit")
    @ResponseBody
    public Result<?> edit(@RequestParam(value = "id") final Long id,
                          @RequestParam(value = "name") final String name,
                          @RequestParam(value = "superAdmin") final boolean superAdmin,
                          @RequestParam(value = "remark") final String remark) throws Exception {
        this.roleService.update(id, name, superAdmin, remark);
        return Result.ok();
    }

    @PostMapping("del")
    @ResponseBody
    public Result<?> del(@RequestParam(value = "ids") final String ids) throws Exception {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        final Set<Long> idSet = new HashSet<>();
        for (final Long id : idList) {
            final List<SysAdmin> sysAdminList = this.adminService.getByRoleId(id);
            if (null != sysAdminList && !sysAdminList.isEmpty()) {
                final SysRole sysRole = this.roleService.getById(id);
                return Result.error(String.format("角色[%s]有管理员正在使用,无法删除", sysRole.getName()));
            }
            idSet.add(id);
        }
        this.roleService.removeByIds(idSet);
        return Result.ok();
    }
}