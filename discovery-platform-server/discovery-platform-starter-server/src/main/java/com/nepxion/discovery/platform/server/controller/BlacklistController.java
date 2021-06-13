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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.entity.dto.BlacklistDto;
import com.nepxion.discovery.platform.server.entity.po.BlacklistPo;
import com.nepxion.discovery.platform.server.entity.po.ListSearchGatewayPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.BlacklistService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("服务无损屏蔽接口")
@RestController
@RequestMapping(BlacklistController.PREFIX)
public class BlacklistController {
    public static final String PREFIX = "blacklist";

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @Autowired
    private BlacklistService blacklistService;

    @ApiOperation("获取服务无损屏屏蔽的信息列表")
    @PostMapping("do-list")
    public Result<List<BlacklistDto>> doList(ListSearchGatewayPo listSearchGatewayPo) {
        IPage<BlacklistDto> page = blacklistService.page(listSearchGatewayPo.getDescription(), listSearchGatewayPo.getPage(), listSearchGatewayPo.getLimit());
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @ApiOperation("通过服务名称获取所有该服务的信息")
    @PostMapping("do-list-service-metadata")
    public Result<List<InstanceEntity>> doListServiceMetadata(@RequestParam("serviceName") String serviceName) {
        if (StringUtils.isEmpty(serviceName)) {
            return Result.ok(new ArrayList<>());
        }
        return Result.ok(platformDiscoveryAdapter.getInstanceList(serviceName));
    }

    @ApiOperation("添加黑名单")
    @PostMapping("do-insert")
    public Result<?> doInsert(BlacklistPo blacklistPo) throws Exception {
        blacklistService.insert(blacklistPo);
        return Result.ok();
    }

    @ApiOperation("启用黑名单")
    @ApiImplicitParam(name = "id", value = "路由id", required = true, dataType = "String")
    @PostMapping("do-enable")
    public Result<?> doEnable(@RequestParam(value = "id") Long id) {
        blacklistService.enable(id, true);
        return Result.ok();
    }

    @ApiOperation("禁用黑名单")
    @ApiImplicitParam(name = "id", value = "路由id", required = true, dataType = "String")
    @PostMapping("do-disable")
    public Result<?> doDisable(@RequestParam(value = "id") Long id) {
        blacklistService.enable(id, false);
        return Result.ok();
    }

    @ApiOperation("删除黑名单")
    @ApiImplicitParam(name = "ids", value = "路由id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        blacklistService.logicDelete(new HashSet<>(idList));
        return Result.ok();
    }

    @ApiOperation("发布黑名单")
    @PostMapping("do-publish")
    public Result<?> doPublish() throws Exception {
        blacklistService.publish();
        return Result.ok();
    }
}