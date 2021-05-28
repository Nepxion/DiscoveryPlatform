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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;
import com.nepxion.discovery.platform.server.service.PageService;
import com.nepxion.discovery.platform.server.service.PermissionService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

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
    public String list(Model model) throws Exception {
        model.addAttribute("roles", this.roleService.getNotSuperAdmin());
        model.addAttribute("pages", this.pageService.listNotEmptyUrlPages());
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("add")
    public String add(Model model) throws Exception {
        model.addAttribute("roles", this.roleService.getNotSuperAdmin());
        return String.format("%s/%s", PREFIX, "add");
    }

    @PostMapping("do-get-pages")
    @ResponseBody
    public Result<List<SysPageDto>> doGetPages(@RequestParam(value = "sysRoleId") Long sysRoleId) throws Exception {
        List<SysPageDto> allPages = this.pageService.list();
        List<SysPageDto> pages = this.permissionService.listPermissionPagesByRoleId(sysRoleId);
        allPages.removeAll(pages);
        return Result.ok(allPages.stream().filter(p -> !ObjectUtils.isEmpty(p.getUrl())).collect(Collectors.toList()));
    }

    @PostMapping("do-list")
    @ResponseBody
    public Result<List<PermissionVo>> doList(@RequestParam(value = "page") Integer pageNum,
                                             @RequestParam(value = "limit") Integer pageSize,
                                             @RequestParam(value = "sysRoleId", required = false) Long sysRoleId,
                                             @RequestParam(value = "sysPageId", required = false) Long sysPageId) throws Exception {
        IPage<PermissionVo> list = this.permissionService.list(pageNum, pageSize, sysRoleId, sysPageId);
        return Result.ok(list.getRecords(), list.getTotal());
    }


    @PostMapping("do-add")
    @ResponseBody
    public Result<?> doAdd(@RequestParam(value = "sysRoleId") Long sysRoleId,
                           @RequestParam(value = "sysPageId") Long sysPageId,
                           @RequestParam(value = "insert", defaultValue = "false") Boolean insert,
                           @RequestParam(value = "delete", defaultValue = "false") Boolean delete,
                           @RequestParam(value = "update", defaultValue = "false") Boolean update,
                           @RequestParam(value = "select", defaultValue = "false") Boolean select) {
        SysPermissionDto authPermission = new SysPermissionDto();
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
    public Result<?> doEdit(@RequestParam(value = "id") Long id,
                            @RequestParam(value = "type") String type,
                            @RequestParam(value = "hasPermission") Boolean hasPermission) {
        SysPermissionDto dbAdminPermission = this.permissionService.getById(id);
        if (dbAdminPermission != null) {
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
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.permissionService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }
}