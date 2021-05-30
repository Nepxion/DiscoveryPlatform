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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysMenuDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.po.MenuPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.MenuVo;
import com.nepxion.discovery.platform.server.service.MenuService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("菜单相关接口")
@RestController
@RequestMapping(MenuController.PREFIX)
public class MenuController {
    public static final String PREFIX = "menu";

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "获取菜单信息列表")
    @PostMapping("do-list")
    public Result<List<MenuVo>> doList(ListSearchNamePo listSearchNamePo) throws Exception {
        IPage<MenuVo> menuVoIPage = menuService.list(listSearchNamePo.getName(), listSearchNamePo.getPage(), listSearchNamePo.getLimit());
        return Result.ok(menuVoIPage.getRecords(), menuVoIPage.getTotal());
    }

    @ApiOperation(value = "添加菜单")
    @PostMapping("do-insert")
    public Result<?> doInsert(MenuPo menuPo) throws Exception {
        if (menuPo.getDefaultFlag() == null) {
            menuPo.setDefaultFlag(false);
        }
        if (menuPo.getShowFlag() == null) {
            menuPo.setShowFlag(false);
        }
        if (menuPo.getBlankFlag() == null) {
            menuPo.setBlankFlag(false);
        }
        Long order = menuService.getMaxOrder(menuPo.getParentId());
        menuPo.setOrder(order + 1);
        menuService.insert(menuPo);
        return Result.ok();
    }

    @ApiOperation(value = "更新菜单")
    @PostMapping("do-update")
    public Result<?> doUpdate(MenuPo menuPo) throws Exception {
        SysMenuDto dbSysMenuDto = menuService.getById(menuPo.getId());
        if (dbSysMenuDto != null) {
            if (menuPo.getDefaultFlag() == null) {
                menuPo.setDefaultFlag(false);
            }
            if (menuPo.getShowFlag() == null) {
                menuPo.setShowFlag(false);
            }
            if (menuPo.getBlankFlag() == null) {
                menuPo.setBlankFlag(false);
            }
            menuService.updateById(menuPo);
        }
        return Result.ok();
    }

    @ApiOperation(value = "删除菜单")
    @ApiImplicitParam(name = "ids", value = "菜单id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        menuService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }
}