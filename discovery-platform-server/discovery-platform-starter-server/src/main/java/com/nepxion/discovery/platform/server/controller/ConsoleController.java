package com.nepxion.discovery.platform.server.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.service.ConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Nepxion Console相关接口")
@RestController
@RequestMapping("/console")
public class ConsoleController {

    @Autowired
    private ConsoleService consoleService;

    @ApiOperation("获取注册中心类型")
    @GetMapping("/discovery-type")
    public Result<String> discoveryType() {
        String discoveryType = consoleService.getDiscoveryType();
        return Result.ok(discoveryType);
    }

    @ApiOperation("获取配置中心类型")
    @GetMapping("/config-type")
    public Result<String> configType() {
        String configType = consoleService.getConfigType();

        return Result.ok(configType);
    }

}
