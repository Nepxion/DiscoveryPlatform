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
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyBlacklistEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.dto.BlacklistDto;
import com.nepxion.discovery.platform.server.entity.po.ListSearchGatewayPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.BlacklistVo;
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
    public Result<?> doInsert(BlacklistDto blacklistDto) throws Exception {
        blacklistService.insert(blacklistDto);
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

    @ApiOperation("获取所有网关的名称")
    @PostMapping("do-list-gateway-names")
    public Result<List<String>> doListGatewayNames() {
        return Result.ok(platformDiscoveryAdapter.getGatewayNames());
    }

    @ApiOperation("获取网关正在工作的黑名单信息")
    @ApiImplicitParam(name = "gatewayName", value = "网关名称", required = true, dataType = "String")
    @PostMapping("do-list-working")
    public Result<Map<String, List<BlacklistVo>>> doList(@RequestParam(value = "gatewayName", required = true, defaultValue = StringUtils.EMPTY) String gatewayName) {
        if (StringUtils.isEmpty(gatewayName)) {
            return Result.ok();
        }
        Map<String, List<BlacklistVo>> blacklistVoMap = new LinkedHashMap<>();

        List<ResultEntity> resultEntityList = platformDiscoveryAdapter.viewConfig(gatewayName);

        for (ResultEntity resultEntity : resultEntityList) {
            String key = String.format("%s:%s", resultEntity.getHost(), resultEntity.getPort());
            List<BlacklistVo> valueList = new ArrayList<>();

            List<String> list = JsonUtil.fromJson(resultEntity.getResult(), List.class);
            RuleEntity ruleEntity = platformDiscoveryAdapter.toRuleEntity(list.get(2));
            StrategyBlacklistEntity strategyBlacklistEntity = ruleEntity.getStrategyBlacklistEntity();

            if (strategyBlacklistEntity != null) {
                String addressValue = strategyBlacklistEntity.getAddressValue();
                if (StringUtils.isNotEmpty(addressValue)) {
                    Map<String, String> map = JsonUtil.fromJson(addressValue, Map.class);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        for (String value : entry.getValue().split(DiscoveryConstant.SEPARATE)) {
                            if (StringUtils.isEmpty(value)) {
                                continue;
                            }
                            BlacklistVo blacklistVo = new BlacklistVo();
                            blacklistVo.setGatewayName(gatewayName);
                            blacklistVo.setServiceName(entry.getKey());
                            blacklistVo.setServiceBlacklistType(BlacklistDto.Type.ADDRESS.getCode());
                            blacklistVo.setServiceBlacklist(value);
                            valueList.add(blacklistVo);
                        }
                    }
                }

                String idValue = strategyBlacklistEntity.getIdValue();
                if (StringUtils.isNotEmpty(idValue)) {
                    Map<String, String> map = JsonUtil.fromJson(idValue, Map.class);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        for (String value : entry.getValue().split(DiscoveryConstant.SEPARATE)) {
                            if (StringUtils.isEmpty(value)) {
                                continue;
                            }
                            BlacklistVo blacklistVo = new BlacklistVo();
                            blacklistVo.setGatewayName(gatewayName);
                            blacklistVo.setServiceName(entry.getKey());
                            blacklistVo.setServiceBlacklistType(BlacklistDto.Type.UUID.getCode());
                            blacklistVo.setServiceBlacklist(value);
                            valueList.add(blacklistVo);
                        }
                    }
                }
            }
            blacklistVoMap.put(key, valueList);
        }

        return Result.ok(blacklistVoMap);
    }

    @ApiOperation("获取所有网关的名称")
    @PostMapping("do-list-portal-names")
    public Result<List<String>> doListPortalNames(@RequestParam("portalType") Integer portalTypeInt) {
        BaseStateEntity.PortalType portalType = BaseStateEntity.PortalType.get(portalTypeInt);
        List<String> result = new ArrayList<>();
        switch (Objects.requireNonNull(portalType)) {
            case GATEWAY:
                result.addAll(platformDiscoveryAdapter.getGatewayNames());
                break;
            case SERVICE:
                result.addAll(platformDiscoveryAdapter.getServiceNames());
                break;
            case GROUP:
                List<String> serviceNames = platformDiscoveryAdapter.getAllServiceNames();
                result.addAll(serviceNames.stream().map(serviceName -> platformDiscoveryAdapter.getGroupName(serviceName)).collect(Collectors.toList()));
                break;
        }
        return Result.ok(result.stream().distinct().sorted(Comparator.naturalOrder()).collect(Collectors.toList()));
    }

    @ApiOperation("获取所有服务的名称")
    @PostMapping("do-list-service-names")
    public Result<List<String>> doListServiceNames() {
        return Result.ok(platformDiscoveryAdapter.getServiceNames());
    }

}