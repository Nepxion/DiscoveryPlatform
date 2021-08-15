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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.InspectorEntity;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.common.entity.InstanceEntityWrapper;
import com.nepxion.discovery.common.entity.PortalType;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.common.util.RestUtil;
import com.nepxion.discovery.console.resource.ServiceResource;
import com.nepxion.discovery.platform.server.entity.dto.GraphDto;
import com.nepxion.discovery.platform.server.entity.po.RequestSimulationPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.InstanceMetaVo;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;
import com.nepxion.discovery.platform.server.tool.StrategyGraphTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("流量侦测")
@RestController
@RequestMapping(InspectorController.PREFIX)
public class InspectorController {

    public static final Logger LOG = LoggerFactory.getLogger(InspectorController.class);

    public static final String PREFIX = "/inspector";

    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            5,
            Runtime.getRuntime().availableProcessors() << 3,
            30L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(2000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceResource serviceResource;

    @ApiOperation("请求模拟")
    @PostMapping("/simulation")
    public Result<GraphDto> simulation(@RequestBody RequestSimulationPo requestSimulationPo) throws ExecutionException, InterruptedException {
        // Header
        Map<String, String> headerMap = requestSimulationPo.getHeaders();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestUtil.processHeader(httpHeaders, headerMap);

        // Parameter
        Map<String, String> parameterMap = requestSimulationPo.getParameters();
        String url = generateUrl(requestSimulationPo, parameterMap);

        // Cookie
        Map<String, String> cookieMap = requestSimulationPo.getCookies();
        RestUtil.processCookie(httpHeaders, cookieMap);

        // Body
        InspectorEntity inspectorEntity = new InspectorEntity();
        List<String> serviceChain = requestSimulationPo.getServiceChain();
        inspectorEntity.setServiceIdList(serviceChain);

        List<Future<InspectorEntity>> futures = new ArrayList<>();
        HttpEntity<InspectorEntity> httpEntity = new HttpEntity<>(inspectorEntity, httpHeaders);
        Integer requestTimes = requestSimulationPo.getRequestTimes();
        for (int i = 0; i < requestTimes; i++) {
            Future<InspectorEntity> future = threadPoolExecutor.submit(() -> {
                String result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();
                return RestUtil.fromJson(restTemplate, result, new TypeReference<InspectorEntity>() {
                });
            });
            futures.add(future);
        }

        List<InspectorEntity> inspectorResults = new ArrayList<>();
        for (Future<InspectorEntity> future : futures) {
            try {
                InspectorEntity inspectorResp = future.get();
                inspectorResults.add(inspectorResp);
            } catch (Exception e) {

            }
        }

        Map<String, Integer> resultCounting = new HashMap<>(16);
        for (InspectorEntity inspectorResult : inspectorResults) {
            String result = inspectorResult.getResult();
            Integer count = resultCounting.getOrDefault(result, 0);
            resultCounting.put(result, count + 1);
        }

        // Result Parse
        Map<String, String> instanceMetaMap = new HashMap<>(16);
        List<List<InstanceMetaVo>> chainCountingList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : resultCounting.entrySet()) {
            String[] instanceInfoArray = StringUtils.splitByWholeSeparator(entry.getKey(), " -> ");
            List<InstanceMetaVo> instanceMetaList = new ArrayList<>();
            for (String instanceInfo : instanceInfoArray) {
                String[] keyValueStringArray = StringUtils.split(instanceInfo, "][");
                for (String keyValueString : keyValueStringArray) {
                    String[] keyValue = StringUtils.split(keyValueString, "=");
                    instanceMetaMap.put(keyValue[0], keyValue[1]);
                }
                instanceMetaMap.put("count", String.valueOf(entry.getValue()));
                InstanceMetaVo instanceMeta = JsonUtil.fromJson(JsonUtil.toJson(instanceMetaMap), InstanceMetaVo.class);
                instanceMetaList.add(instanceMeta);
            }
            chainCountingList.add(instanceMetaList);
        }

        GraphDto graphDto = StrategyGraphTool.convertInspectResultToGraph(requestSimulationPo.getDimension(),
                requestSimulationPo.getRequestTimes(), chainCountingList);
        return Result.ok(graphDto);
    }

    public String generateUrl(RequestSimulationPo requestSimulationPo, Map<String, String> parameterMap) {
        PortalType portalType = requestSimulationPo.getPortalType();
        String protocol = requestSimulationPo.getPortalProtocol();

        String url = requestSimulationPo.getPortalInstance();
        if (portalType == PortalType.GATEWAY) {
            List<String> serviceChain = requestSimulationPo.getServiceChain();
            String serviceId = serviceChain.remove(0);
            url = protocol + url + "/" + serviceId + getContextPath(serviceId) + DiscoveryConstant.INSPECTOR_ENDPOINT_URL;
        } else {
            String serviceId = requestSimulationPo.getPortalInstance();
            url = protocol + url + getContextPath(serviceId) + DiscoveryConstant.INSPECTOR_ENDPOINT_URL;
        }
        return RestUtil.processParameter(url, parameterMap);
    }

    public String getContextPath(String serviceId) {
        try {
            List<InstanceEntity> instanceList = getInstanceList(serviceId);
            if (CollectionUtils.isEmpty(instanceList)) {
                LOG.warn("No service from register center.");
                return "/";
            }
            return InstanceEntityWrapper.getFormatContextPath(instanceList.get(0));
        } catch (Exception e) {
            return "/";
        }
    }

    public List<InstanceEntity> getInstanceList(String serviceId) {
        try {
            return serviceResource.getInstanceList(serviceId);
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e));
            return Collections.emptyList();
        }
    }

}
