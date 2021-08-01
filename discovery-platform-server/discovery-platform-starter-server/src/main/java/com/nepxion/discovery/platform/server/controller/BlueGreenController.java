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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchNamePo;
import com.nepxion.discovery.platform.server.entity.po.StrategyPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("蓝绿发布相关接口")
@RestController
@RequestMapping(BlueGreenController.PREFIX)
public class BlueGreenController extends StrategyController {
    public static final String PREFIX = "blue-green";

    @ApiOperation("获取蓝绿发布信息列表")
    @PostMapping("do-list")
    public Result<List<BlueGreenDto>> doList(ListSearchNamePo listSearchNamePo) {
        IPage<BlueGreenDto> blueGreenDtoPage = blueGreenService.page(listSearchNamePo.getName(), listSearchNamePo.getPage(), listSearchNamePo.getLimit());
        return Result.ok(blueGreenDtoPage.getRecords(), blueGreenDtoPage.getTotal());
    }

    @SuppressWarnings("unchecked")
    @ApiOperation("获取Spring Cloud Gateway网关正在工作的蓝绿信息")
    @ApiImplicitParam(name = "gatewayName", value = "网关名称", required = true, dataType = "String")
    @PostMapping("do-list-working")
    public Result<Map<String, String>> doListWorking(@RequestParam(value = "portalType", required = true, defaultValue = StringUtils.EMPTY) String portalType,
                                                     @RequestParam(value = "gatewayName", required = true, defaultValue = StringUtils.EMPTY) String gatewayName) throws Exception {
        if (StringUtils.isEmpty(portalType) || StringUtils.isEmpty(gatewayName)) {
            return Result.ok();
        }

        BaseStateEntity.PortalType portalTypeEnum = BaseStateEntity.PortalType.valueOf(portalType);
        Map<String, String> result = new LinkedHashMap<>();

        switch (portalTypeEnum) {
            case GATEWAY:
            case SERVICE:
                List<ResultEntity> resultEntityList = platformDiscoveryAdapter.viewConfig(gatewayName);
                for (ResultEntity resultEntity : resultEntityList) {
                    String key = String.format("%s:%s", resultEntity.getHost(), resultEntity.getPort());
                    List<String> list = JsonUtil.fromJson(resultEntity.getResult(), List.class);
                    result.put(key, list.get(2));
                }
                break;
            case GROUP:
                result.put("组", platformDiscoveryAdapter.viewConfigByGroupName(gatewayName));
                break;
        }
        return Result.ok(result);
    }

    @ApiOperation("新增蓝绿信息")
    @PostMapping("do-insert")
    public Result<Boolean> doInsert(StrategyPo strategyPo) {
        return Result.ok(blueGreenService.insert(strategyPo));
    }

    @ApiOperation("修改蓝绿信息")
    @PostMapping("do-update")
    public Result<?> doUpdate(StrategyPo strategyPo) {
        if (blueGreenService.update(strategyPo)) {
            return Result.ok();
        }
        return Result.error("更新失败");
    }

    @ApiOperation("启用蓝绿")
    @ApiImplicitParam(name = "id", value = "蓝绿id", required = true, dataType = "String")
    @PostMapping("do-enable")
    public Result<?> doEnable(@RequestParam(value = "id") Long id) {
        blueGreenService.enable(id, true);
        return Result.ok();
    }

    @ApiOperation("禁用蓝绿")
    @ApiImplicitParam(name = "id", value = "蓝绿id", required = true, dataType = "String")
    @PostMapping("do-disable")
    public Result<?> doDisable(@RequestParam(value = "id") Long id) {
        blueGreenService.enable(id, false);
        return Result.ok();
    }

    @ApiOperation("删除蓝绿")
    @ApiImplicitParam(name = "ids", value = "蓝绿id, 多个用逗号分隔", required = true, dataType = "String")
    @PostMapping("do-delete")
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        blueGreenService.logicDelete(new HashSet<>(idList));
        return Result.ok();
    }
}