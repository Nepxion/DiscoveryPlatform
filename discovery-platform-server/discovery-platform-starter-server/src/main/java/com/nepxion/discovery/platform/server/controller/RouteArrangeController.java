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
import com.nepxion.discovery.platform.server.entity.dto.RouteArrangeDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.RouteArrangeService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("链路编排相关接口")
@RestController
@RequestMapping(RouteArrangeController.PREFIX)
public class RouteArrangeController {
    public static final String PREFIX = "route-arrange";

    @Autowired
    private RouteArrangeService routeArrangeService;

    @ApiOperation("获取链路列表")
    @RequestMapping("do-list")
    public Result<List<RouteArrangeDto>> doList(ListSearchNamePo listSearchNamePo) {
        IPage<RouteArrangeDto> routeArrangeDtoPage = routeArrangeService.page(listSearchNamePo.getName(), listSearchNamePo.getPage(), listSearchNamePo.getLimit());
        return Result.ok(routeArrangeDtoPage.getRecords(), routeArrangeDtoPage.getTotal());
    }

    @ApiOperation("新增链路")
    @PostMapping("do-insert")
    public Result<Boolean> doInsert(RouteArrangeDto routeArrangeDto) {
        return Result.ok(routeArrangeService.insert(routeArrangeDto));
    }

    @ApiOperation("修改链路")
    @PostMapping("do-update")
    public Result<?> doUpdate(RouteArrangeDto routeArrangeDto) throws Exception {
        if (routeArrangeService.update(routeArrangeDto)) {
            return Result.ok();
        }
        return Result.error("更新失败");
    }

    @ApiOperation("删除链路")
    @ApiImplicitParam(name = "ids", value = "链路id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        routeArrangeService.logicDelete(new HashSet<>(idList));
        return Result.ok();
    }

    @ApiOperation("发布链路")
    @PostMapping("do-publish")
    public Result<?> doPublish() throws Exception {
        routeArrangeService.publish();
        return Result.ok();
    }
}