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
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.dto.SysRoleDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("角色接口")
@RestController
@RequestMapping(RoleController.PREFIX)
public class RoleController {
    public static final String PREFIX = "role";
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "获取角色信息列表")
    @PostMapping("do-list")
    public Result<List<SysRoleDto>> doList(ListSearchNamePo listSearchNamePo) throws Exception {
        IPage<SysRoleDto> sysAdmins = roleService.list(listSearchNamePo.getName(), listSearchNamePo.getPage(), listSearchNamePo.getLimit());
        return Result.ok(sysAdmins.getRecords(), sysAdmins.getTotal());
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("do-insert")
    public Result<?> doInsert(SysRoleDto sysRoleDto) throws Exception {
        roleService.insert(sysRoleDto.getName(), sysRoleDto.getSuperAdmin(), sysRoleDto.getDescription());
        return Result.ok();
    }

    @ApiOperation(value = "更新角色")
    @PostMapping("do-update")
    public Result<?> doUpdate(SysRoleDto sysRoleDto) throws Exception {
        roleService.update(sysRoleDto.getId(), sysRoleDto.getName(), sysRoleDto.getSuperAdmin(), sysRoleDto.getDescription());
        return Result.ok();
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(name = "ids", value = "角色id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) throws Exception {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        Set<Long> idSet = new HashSet<>();
        for (Long id : idList) {
            List<SysAdminDto> sysAdminList = adminService.getByRoleId(id);
            if (!CollectionUtils.isEmpty(sysAdminList)) {
                SysRoleDto sysRole = roleService.getById(id);
                return Result.error(String.format("角色[%s]有管理员正在使用,无法删除", sysRole.getName()));
            }
            idSet.add(id);
        }
        roleService.removeByIds(idSet);
        return Result.ok();
    }
}