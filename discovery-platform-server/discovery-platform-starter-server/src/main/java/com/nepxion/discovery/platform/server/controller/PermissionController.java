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
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;
import com.nepxion.discovery.platform.server.service.PageService;
import com.nepxion.discovery.platform.server.service.PermissionService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PageService pageService;

    @GetMapping("list")
    public String list(final Model model) throws Exception {
        model.addAttribute("roles", this.roleService.getNotSuperAdmin());
        model.addAttribute("pages", this.pageService.listNotEmptyUrlPages());
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("add")
    public String add(final Model model) throws Exception {
        model.addAttribute("roles", this.roleService.getNotSuperAdmin());
        return String.format("%s/%s", PREFIX, "add");
    }

    @PostMapping("do-get-pages")
    @ResponseBody
    public Result<List<SysPageDto>> doGetPages(@RequestParam(value = "sysRoleId") final Long sysRoleId) throws Exception {
        final List<SysPageDto> allPages = this.pageService.list();
        final List<SysPageDto> pages = this.permissionService.listPermissionPagesByRoleId(sysRoleId);
        allPages.removeAll(pages);
        return Result.ok(allPages.stream().filter(p -> !ObjectUtils.isEmpty(p.getUrl())).collect(Collectors.toList()));
    }

    @PostMapping("do-list")
    @ResponseBody
    public Result<List<PermissionVo>> doList(@RequestParam(value = "page") final Integer pageNum,
                                             @RequestParam(value = "limit") final Integer pageSize,
                                             @RequestParam(value = "sysRoleId", required = false) final Long sysRoleId,
                                             @RequestParam(value = "sysPageId", required = false) final Long sysPageId) throws Exception {
        final IPage<PermissionVo> list = this.permissionService.list(pageNum, pageSize, sysRoleId, sysPageId);
        return Result.ok(list.getRecords(), list.getTotal());
    }


    @PostMapping("do-add")
    @ResponseBody
    public Result<?> doAdd(@RequestParam(value = "sysRoleId") final Long sysRoleId,
                           @RequestParam(value = "sysPageId") final Long sysPageId,
                           @RequestParam(value = "insert", defaultValue = "false") final Boolean insert,
                           @RequestParam(value = "delete", defaultValue = "false") final Boolean delete,
                           @RequestParam(value = "update", defaultValue = "false") final Boolean update,
                           @RequestParam(value = "select", defaultValue = "false") final Boolean select) {
        final SysPermissionDto authPermission = new SysPermissionDto();
        authPermission.setSysRoleId(sysRoleId);
        authPermission.setSysPageId(sysPageId);
        authPermission.setCanInsert(insert);
        authPermission.setCanDelete(delete);
        authPermission.setCanUpdate(update);
        authPermission.setCanSelect(select);
        this.permissionService.insert(authPermission);
        return Result.ok();
    }

    @PostMapping("do-edit")
    @ResponseBody
    public Result<?> doEdit(@RequestParam(value = "id") final Long id,
                            @RequestParam(value = "type") final String type,
                            @RequestParam(value = "hasPermission") final Boolean hasPermission) {
        final SysPermissionDto dbAdminPermission = this.permissionService.getById(id);
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

    @PostMapping("do-delete")
    @ResponseBody
    public Result<?> doDelete(@RequestParam(value = "ids") final String ids) {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.permissionService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }
}