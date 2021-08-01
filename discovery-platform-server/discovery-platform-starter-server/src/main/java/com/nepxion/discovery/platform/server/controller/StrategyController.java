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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.BlueGreenService;
import io.swagger.annotations.ApiOperation;

public abstract class StrategyController<T> {
    @Autowired
    protected BlueGreenService blueGreenService;
    @Autowired
    protected PlatformDiscoveryAdapter platformDiscoveryAdapter;

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
            List<String> portNameList = blueGreenService.listPortalNames();
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

    @ApiOperation("发布蓝绿")
    @PostMapping("do-publish")
    public Result<?> doPublish() throws Exception {
        blueGreenService.publish();
        return Result.ok();
    }
}