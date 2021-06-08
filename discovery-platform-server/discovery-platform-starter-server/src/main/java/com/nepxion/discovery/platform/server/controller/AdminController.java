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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.adapter.PlatformLoginAdapter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.po.AdminPo;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("管理员相关接口")
@RestController
@RequestMapping(AdminController.PREFIX)
public class AdminController {
    public static final String PREFIX = "admin";

    @Autowired
    private AdminService adminService;

    @Autowired
    private PlatformLoginAdapter loginAdapter;

    @ApiOperation("获取管理员信息列表")
    @PostMapping("do-list")
    public Result<List<AdminVo>> doList(ListSearchNamePo listSearchNamePo) throws Exception {
        IPage<AdminVo> adminPage = adminService.list(loginAdapter.getLoginMode(), listSearchNamePo.getName(), listSearchNamePo.getPage(), listSearchNamePo.getLimit());
        return Result.ok(adminPage.getRecords(), adminPage.getTotal());
    }

    @ApiOperation("重置管理员的密码")
    @PostMapping("do-reset-password")
    public Result<?> doResetPassword(AdminPo adminPo) throws Exception {
        SysAdminDto sysAdmin = adminService.getById(adminPo.getId());
        if (sysAdmin == null) {
            return Result.error(String.format("用户[id=%s]不存在", adminPo.getId()));
        }
        if (adminService.changePassword(adminPo.getId(), sysAdmin.getPassword(), CommonTool.hash(PlatformConstant.DEFAULT_ADMIN_PASSWORD))) {
            return Result.ok();
        } else {
            return Result.error("密码修改失败");
        }
    }

    @ApiOperation("添加管理员")
    @PostMapping("do-insert")
    public Result<?> doInsert(AdminPo adminPo) throws Exception {
        adminService.insert(loginAdapter.getLoginMode(), adminPo);
        return Result.ok();
    }

    @ApiOperation("更新管理员的信息")
    @PostMapping("do-update")
    public Result<?> doUpdate(AdminPo adminPo) throws Exception {
        adminService.update(adminPo);
        return Result.ok();
    }

    @ApiOperation("删除管理员")
    @ApiImplicitParam(name = "ids", value = "管理员的id, 多个用逗号隔开", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        adminService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }

    @ApiOperation("根据关键字查询管理员信息")
    @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String")
    @PostMapping("do-search")
    public Result<List<AdminVo>> doSearch(@RequestParam(value = "keyword", defaultValue = StringUtils.EMPTY) String keyword) {
        if (StringUtils.isEmpty(keyword.trim())) {
            return Result.ok();
        }
        return Result.ok(adminService.search(keyword, 0, 10));
    }
}