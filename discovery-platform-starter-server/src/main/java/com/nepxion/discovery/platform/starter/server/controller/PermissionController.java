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
import com.nepxion.discovery.platform.starter.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.starter.server.entity.dto.SysPermission;
import com.nepxion.discovery.platform.starter.server.entity.vo.Permission;
import com.nepxion.discovery.platform.starter.server.interfaces.PageService;
import com.nepxion.discovery.platform.starter.server.interfaces.PermissionService;
import com.nepxion.discovery.platform.starter.server.interfaces.RoleService;
import com.nepxion.discovery.platform.starter.server.tool.common.CommonTool;
import com.nepxion.discovery.platform.starter.server.tool.web.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(PermissionController.PREFIX)
public class PermissionController {
    public static final String PREFIX = "permission";

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final PageService pageService;

    public PermissionController(final PermissionService permissionService,
                                final RoleService roleService,
                                final PageService pageService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.pageService = pageService;
    }

    @GetMapping("tolist")
    public String toList(final Model model) throws Exception {
        model.addAttribute("roles", this.roleService.getNotSuperAdmin());
        model.addAttribute("pages", this.pageService.listNotEmptyUrlPages());
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toadd")
    public String toAdd(final Model model) throws Exception {
        model.addAttribute("roles", this.roleService.getNotSuperAdmin());
        return String.format("%s/%s", PREFIX, "add");
    }

    @PostMapping("getPages")
    @ResponseBody
    public Result<List<SysPage>> getPages(@RequestParam(value = "sysRoleId") final Long sysRoleId) throws Exception {
        final List<SysPage> allPages = this.pageService.list();
        final List<SysPage> pages = this.permissionService.listPermissionPagesByRoleId(sysRoleId);
        allPages.removeAll(pages);
        return Result.ok(allPages.stream().filter(p -> !ObjectUtils.isEmpty(p.getUrl())).collect(Collectors.toList()));
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<Permission>> list(@RequestParam(value = "page") final Integer pageNum,
                                         @RequestParam(value = "limit") final Integer pageSize,
                                         @RequestParam(value = "sysRoleId", required = false) final Long sysRoleId,
                                         @RequestParam(value = "sysPageId", required = false) final Long sysPageId) throws Exception {
        final IPage<Permission> list = this.permissionService.list(pageNum, pageSize, sysRoleId, sysPageId);
        return Result.ok(list.getRecords(), list.getTotal());
    }


    @PostMapping("add")
    @ResponseBody
    public Result<?> add(@RequestParam(value = "sysRoleId") final Long sysRoleId,
                         @RequestParam(value = "sysPageId") final Long sysPageId,
                         @RequestParam(value = "insert", defaultValue = "false") final Boolean insert,
                         @RequestParam(value = "delete", defaultValue = "false") final Boolean delete,
                         @RequestParam(value = "update", defaultValue = "false") final Boolean update,
                         @RequestParam(value = "select", defaultValue = "false") final Boolean select) throws Exception {
        final SysPermission authPermission = new SysPermission();
        authPermission.setSysRoleId(sysRoleId);
        authPermission.setSysPageId(sysPageId);
        authPermission.setCanInsert(insert);
        authPermission.setCanDelete(delete);
        authPermission.setCanUpdate(update);
        authPermission.setCanSelect(select);
        this.permissionService.insert(authPermission);
        return Result.ok();
    }

    @PostMapping("edit")
    @ResponseBody
    public Result<?> edit(@RequestParam(value = "id") final Long id,
                          @RequestParam(value = "type") final String type,
                          @RequestParam(value = "hasPermission") final Boolean hasPermission) throws Exception {
        final SysPermission dbAdminPermission = this.permissionService.getById(id);
        if (null != dbAdminPermission) {
            switch (type.toLowerCase()) {
                case "insert":
                    dbAdminPermission.setCanInsert(hasPermission);
                    break;
                case "delete":
                    dbAdminPermission.setCanDelete(hasPermission);
                    break;
                case "update":
                    dbAdminPermission.setCanUpdate(hasPermission);
                    break;
                case "select":
                    dbAdminPermission.setCanSelect(hasPermission);
                    break;
            }
            this.permissionService.updateById(dbAdminPermission);
        }
        return Result.ok();
    }

    @PostMapping("del")
    @ResponseBody
    public Result<?> del(@RequestParam(value = "ids") final String ids) throws Exception {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.permissionService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }
}