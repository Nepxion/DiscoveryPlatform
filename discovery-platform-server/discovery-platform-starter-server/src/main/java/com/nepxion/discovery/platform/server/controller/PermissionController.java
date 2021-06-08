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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysMenuDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.entity.po.PermissionPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;
import com.nepxion.discovery.platform.server.service.MenuService;
import com.nepxion.discovery.platform.server.service.PermissionService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api("权限相关接口")
@RestController
@RequestMapping(PermissionController.PREFIX)
public class PermissionController {
    public static final String PREFIX = "permission";

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuService menuService;

    @ApiOperation("获取指定角色id的所有菜单")
    @ApiImplicitParam(name = "sysRoleId", value = "角色id", required = true, dataType = "String")
    @PostMapping("do-get-menus")
    public Result<List<SysMenuDto>> doGetMenus(@RequestParam(value = "sysRoleId") Long sysRoleId) throws Exception {
        List<SysMenuDto> allMenus = menuService.list();
        List<SysMenuDto> menus = permissionService.listPermissionMenusByRoleId(sysRoleId);
        allMenus.removeAll(menus);
        return Result.ok(allMenus.stream().filter(p -> StringUtils.isNotEmpty(p.getUrl())).collect(Collectors.toList()));
    }

    @ApiOperation("获取权限信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sysRoleId", value = "角色ID", required = false, dataType = "Long"),
            @ApiImplicitParam(name = "sysMenuId", value = "菜单ID", required = false, dataType = "Long")
    })
    @PostMapping("do-list")
    public Result<List<PermissionVo>> doList(@RequestParam(value = "page") Integer pageNum, @RequestParam(value = "limit") Integer pageSize, @RequestParam(value = "sysRoleId", required = false) Long sysRoleId, @RequestParam(value = "sysMenuId", required = false) Long sysMenuId) throws Exception {
        IPage<PermissionVo> list = permissionService.list(pageNum, pageSize, sysRoleId, sysMenuId);
        return Result.ok(list.getRecords(), list.getTotal());
    }

    @ApiOperation("添加权限")
    @PostMapping("do-insert")
    public Result<?> doInsert(PermissionPo permissionPo) {
        SysPermissionDto sysPermissionDto = new SysPermissionDto();
        sysPermissionDto.setSysRoleId(permissionPo.getSysRoleId());
        sysPermissionDto.setSysMenuId(permissionPo.getSysMenuId());
        sysPermissionDto.setCanInsert("on".equalsIgnoreCase(permissionPo.getInsert()));
        sysPermissionDto.setCanDelete("on".equalsIgnoreCase(permissionPo.getDelete()));
        sysPermissionDto.setCanUpdate("on".equalsIgnoreCase(permissionPo.getUpdate()));
        sysPermissionDto.setCanSelect("on".equalsIgnoreCase(permissionPo.getSelect()));
        permissionService.insert(sysPermissionDto);
        return Result.ok();
    }

    @ApiOperation("更新权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "type", value = "权限类型(1:INSERT; 2:UPDATE; 3:DELETE; 4:SELECT)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "hasPermission", value = "是否拥有该项权限", required = true, dataType = "Boolean")
    })
    @PostMapping("do-update")
    public Result<?> doUpdate(@RequestParam(value = "id") Long id, @RequestParam(value = "type") String type, @RequestParam(value = "hasPermission") Boolean hasPermission) {
        SysPermissionDto dbAdminPermission = permissionService.getById(id);
        if (dbAdminPermission != null) {
            Operation operation = Operation.get(type);
            switch (Objects.requireNonNull(operation)) {
                case INSERT:
                    dbAdminPermission.setCanInsert(hasPermission);
                    break;
                case DELETE:
                    dbAdminPermission.setCanDelete(hasPermission);
                    break;
                case UPDATE:
                    dbAdminPermission.setCanUpdate(hasPermission);
                    break;
                case SELECT:
                    dbAdminPermission.setCanSelect(hasPermission);
                    break;
            }
            permissionService.updateById(dbAdminPermission);
        }
        return Result.ok();
    }

    @ApiOperation("删除权限")
    @ApiImplicitParam(name = "ids", value = "权限id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        permissionService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }
}