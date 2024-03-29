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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.StrategyDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.po.StrategyPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.StrategyService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

@Api("蓝绿灰度发布相关接口")
@RestController
@RequestMapping(StrategyController.PREFIX)
public class StrategyController {
    public static final String PREFIX = "strategy";

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private PlatformDiscoveryAdapter platformDiscoveryAdapter;

    @ApiOperation("获取所有入口的名称")
    @PostMapping("do-list-portal-names")
    public Result<List<String>> doListPortalNames(@RequestParam(name = "portalName", defaultValue = "") String portalName,
                                                  @RequestParam(value = "portalTypeInt", required = false) Integer portalTypeInt,
                                                  @RequestParam(value = "portalTypeStr", required = false) String portalTypeStr,
                                                  @RequestParam(value = "excludeDb", required = false, defaultValue = "true") Boolean excludeDb) {
        if (portalTypeInt == null && StringUtils.isEmpty(portalTypeStr)) {
            return Result.ok(new ArrayList<>());
        }
        BaseStateEntity.PortalType portalType = null;
        if (portalTypeInt != null) {
            portalType = BaseStateEntity.PortalType.get(portalTypeInt);
        } else if (StringUtils.isNotEmpty(portalTypeStr)) {
            portalType = BaseStateEntity.PortalType.valueOf(portalTypeStr);
        }

        List<String> result = new ArrayList<>();
        switch (Objects.requireNonNull(portalType)) {
            case GATEWAY:
                result.addAll(platformDiscoveryAdapter.getGatewayNames());
                break;
            case SERVICE:
                result.addAll(platformDiscoveryAdapter.getServiceNames());
                break;
            case GROUP:
                result.addAll(platformDiscoveryAdapter.getGroupNames());
                break;
        }
        if (excludeDb) {
            boolean flag = result.contains(portalName);
            List<String> portNameList = strategyService.listPortalNames();
            result.removeAll(portNameList);
            if (StringUtils.isNotEmpty(portalName) && flag) {
                result.add(portalName);
            }
        }
        return Result.ok(result.stream().distinct().sorted(Comparator.naturalOrder()).collect(Collectors.toList()));
    }

    @ApiOperation("校验自定义表达式")
    @PostMapping("validate-expression")
    public Result<Boolean> validateExpression(@RequestParam("expression") String expression, @RequestParam("validation") String validation) {
        return Result.ok(platformDiscoveryAdapter.validateExpression(expression, validation));
    }

    @ApiOperation("发布蓝绿灰度")
    @PostMapping("do-publish")
    public Result<?> doPublish() throws Exception {
        strategyService.publish();
        return Result.ok();
    }

    @ApiOperation("获取蓝绿灰度发布信息列表")
    @PostMapping("do-list")
    public Result<List<StrategyDto>> doList(ListSearchNamePo listSearchNamePo) {
        IPage<StrategyDto> strategyDtoPage = strategyService.page(listSearchNamePo.getName(), listSearchNamePo.getPage(), listSearchNamePo.getLimit());
        return Result.ok(strategyDtoPage.getRecords(), strategyDtoPage.getTotal());
    }

    @SuppressWarnings("unchecked")
    @ApiOperation("获取正在工作的蓝绿灰度信息")
    @ApiImplicitParam()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "portalType", value = "入口类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "portalName", value = "入口名称", required = true, dataType = "String"),
    })
    @PostMapping("do-list-working")
    public Result<Map<String, String>> doListWorking(@RequestParam(value = "portalType", required = true, defaultValue = StringUtils.EMPTY) String portalType,
                                                     @RequestParam(value = "portalName", required = true, defaultValue = StringUtils.EMPTY) String portalName) throws Exception {
        if (StringUtils.isEmpty(portalType) || StringUtils.isEmpty(portalName)) {
            return Result.ok();
        }

        BaseStateEntity.PortalType portalTypeEnum = BaseStateEntity.PortalType.valueOf(portalType);
        Map<String, String> result = new LinkedHashMap<>();

        switch (portalTypeEnum) {
            case GATEWAY:
            case SERVICE:
                List<ResultEntity> resultEntityList = platformDiscoveryAdapter.viewConfig(portalName);
                for (ResultEntity resultEntity : resultEntityList) {
                    String key = String.format("%s:%s", resultEntity.getHost(), resultEntity.getPort());
                    List<String> list = JsonUtil.fromJson(resultEntity.getResult(), List.class);
                    result.put(key, list.get(2));
                }
                break;
            case GROUP:
                result.put("group", platformDiscoveryAdapter.viewConfigByGroupName(portalName));
                break;
        }
        return Result.ok(result);
    }

    @ApiOperation("新增蓝绿灰度信息")
    @PostMapping("do-insert")
    public Result<Boolean> doInsert(StrategyPo strategyPo) {
        return Result.ok(strategyService.insert(strategyPo));
    }

    @ApiOperation("修改蓝绿灰度信息")
    @PostMapping("do-update")
    public Result<?> doUpdate(StrategyPo strategyPo) {
        if (strategyService.update(strategyPo)) {
            return Result.ok();
        }
        return Result.error("更新失败");
    }

    @ApiOperation("启用蓝绿灰度")
    @ApiImplicitParam(name = "id", value = "蓝绿灰度id", required = true, dataType = "String")
    @PostMapping("do-enable")
    public Result<?> doEnable(@RequestParam(value = "id") Long id) throws Exception {
        strategyService.enable(id, true);
        return Result.ok();
    }

    @ApiOperation("禁用蓝绿灰度")
    @ApiImplicitParam(name = "id", value = "蓝绿灰度id", required = true, dataType = "String")
    @PostMapping("do-disable")
    public Result<?> doDisable(@RequestParam(value = "id") Long id) throws Exception {
        strategyService.enable(id, false);
        return Result.ok();
    }

    @ApiOperation("删除蓝绿灰度")
    @ApiImplicitParam(name = "ids", value = "蓝绿灰度id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        strategyService.logicDelete(new HashSet<>(idList));
        return Result.ok();
    }
}