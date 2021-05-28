package com.nepxion.discovery.platform.server.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.dto.SysRoleDto;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

@Controller
@RequestMapping(RoleController.PREFIX)
public class RoleController {
    public static final String PREFIX = "role";
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;

    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("add")
    public String add() {
        return String.format("%s/%s", PREFIX, "add");
    }

    @GetMapping("edit")
    public String edit(Model model,
                       @RequestParam(name = "id") Long id) {
        model.addAttribute("role", this.roleService.getById(id));
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("do-list")
    @ResponseBody
    public Result<List<SysRoleDto>> doList(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "page") Integer pageNum,
                                           @RequestParam(value = "limit") Integer pageSize) throws Exception {
        IPage<SysRoleDto> sysAdmins = this.roleService.list(name, pageNum, pageSize);
        return Result.ok(sysAdmins.getRecords(), sysAdmins.getTotal());
    }

    @PostMapping("do-add")
    @ResponseBody
    public Result<?> doAdd(@RequestParam(value = "name") String name,
                           @RequestParam(value = "superAdmin") boolean superAdmin,
                           @RequestParam(value = "remark") String remark) throws Exception {
        this.roleService.insert(name, superAdmin, remark);
        return Result.ok();
    }

    @PostMapping("do-edit")
    @ResponseBody
    public Result<?> doEdit(@RequestParam(value = "id") Long id,
                            @RequestParam(value = "name") String name,
                            @RequestParam(value = "superAdmin") boolean superAdmin,
                            @RequestParam(value = "remark") String remark) throws Exception {
        this.roleService.update(id, name, superAdmin, remark);
        return Result.ok();
    }

    @PostMapping("do-delete")
    @ResponseBody
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) throws Exception {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        Set<Long> idSet = new HashSet<>();
        for (Long id : idList) {
            List<SysAdminDto> sysAdminList = this.adminService.getByRoleId(id);
            if (!CollectionUtils.isEmpty(sysAdminList)) {
                SysRoleDto sysRole = this.roleService.getById(id);
                return Result.error(String.format("角色[%s]有管理员正在使用,无法删除", sysRole.getName()));
            }
            idSet.add(id);
        }
        this.roleService.removeByIds(idSet);
        return Result.ok();
    }
}