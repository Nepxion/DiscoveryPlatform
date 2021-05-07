package com.nepxion.discovery.platform.application.controller;

import com.nepxion.discovery.console.endpoint.ConsoleEndpoint;
import com.nepxion.discovery.platform.server.ui.tool.common.CommonTool;
import com.nepxion.discovery.platform.server.ui.tool.web.Result;
import com.nepxion.discovery.plugin.admincenter.endpoint.GatewayStrategyRouteEndpoint;
import com.nepxion.discovery.plugin.admincenter.endpoint.ZuulStrategyRouteEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(RouteController.PREFIX)
public class RouteController {
    public static final String PREFIX = "route";
    private final ConsoleEndpoint consoleEndpoint;
    private final GatewayStrategyRouteEndpoint gatewayStrategyRouteEndpoint;
    private final ZuulStrategyRouteEndpoint zuulStrategyRouteEndpoint;

    public RouteController(final ConsoleEndpoint consoleEndpoint,
                           @Autowired(required = false) GatewayStrategyRouteEndpoint gatewayStrategyRouteEndpoint,
                           @Autowired(required = false) ZuulStrategyRouteEndpoint zuulStrategyRouteEndpoint) {
        this.consoleEndpoint = consoleEndpoint;
        this.gatewayStrategyRouteEndpoint = gatewayStrategyRouteEndpoint;
        this.zuulStrategyRouteEndpoint = zuulStrategyRouteEndpoint;
    }

    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toworking")
    public String toWorking() {
        return String.format("%s/%s", PREFIX, "working");
    }

    @GetMapping("toadd")
    public String toAdd(final Model model) {
        model.addAttribute("serviceNames", this.consoleEndpoint.services());
        return String.format("%s/%s", PREFIX, "add");
    }

    @GetMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(name = "id") final String id) {
        model.addAttribute("serviceNames", this.consoleEndpoint.services());
        model.addAttribute("route", this.gatewayStrategyRouteEndpoint.view(id));  //TODO 应该是从配置中心拿
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("listGateway")
    @ResponseBody
    public Result<List<?>> listGateway(@RequestParam(value = "page") final Integer pageNum,
                                                                @RequestParam(value = "limit") final Integer pageSize,
                                                                @RequestParam(value = "description", required = false) final String description) {
        final ResponseEntity<?> responseEntity = gatewayStrategyRouteEndpoint.viewAll(); //TODO 应该是从配置中心拿
        Object body = responseEntity.getBody();
        return Result.ok();
    }

    @PostMapping("listWorking")
    @ResponseBody
    public Result<List<?>> listWorking() {
        final ResponseEntity<?> responseEntity = this.gatewayStrategyRouteEndpoint.viewAll();
        Object body = responseEntity.getBody();
        return Result.ok(null);
    }

    @PostMapping("addGateway")
    @ResponseBody
    public Result<?> addGateway(@RequestParam(value = "id") final String id,
                                @RequestParam(value = "uri") final String uri,
                                @RequestParam(value = "predicates") final String predicates,
                                @RequestParam(value = "filters") final String filters,
                                @RequestParam(value = "order") final Integer order,
                                @RequestParam(value = "metadata") final String metadata,
                                @RequestParam(value = "enabled") final Boolean enabled,
                                @RequestParam(value = "description") final String description) {
//        final GatewayStrategyRouteEntity gatewayStrategyRouteEntity = new GatewayStrategyRouteEntity();
//        gatewayStrategyRouteEntity.setId(id);
//        gatewayStrategyRouteEntity.setUri(uri);
//        gatewayStrategyRouteEntity.setPredicates(Lists.newArrayList());
//        gatewayStrategyRouteEntity.setFilters(Lists.newArrayList());
//        gatewayStrategyRouteEntity.setOrder(order);
//        gatewayStrategyRouteEntity.setMetadata(Maps.newHashMap());
//        this.gatewayStrategyRouteEndpoint.add(gatewayStrategyRouteEntity);
        return Result.ok();
    }

    @PostMapping("editGateway")
    @ResponseBody
    public Result<?> edit(@RequestParam(value = "id") final String id,
                          @RequestParam(value = "uri") final String uri,
                          @RequestParam(value = "predicates") final String predicates,
                          @RequestParam(value = "filters") final String filters,
                          @RequestParam(value = "order") final Integer order,
                          @RequestParam(value = "metadata") final String metadata,
                          @RequestParam(value = "enabled") final Boolean enabled,
                          @RequestParam(value = "description") final String description) {
//        final GatewayStrategyRouteEntity gatewayStrategyRouteEntity = new GatewayStrategyRouteEntity();
//        gatewayStrategyRouteEntity.setId(id);
//        gatewayStrategyRouteEntity.setUri(uri);
//        gatewayStrategyRouteEntity.setPredicates(Lists.newArrayList());
//        gatewayStrategyRouteEntity.setFilters(Lists.newArrayList());
//        gatewayStrategyRouteEntity.setOrder(order);
//        gatewayStrategyRouteEntity.setMetadata(Maps.newHashMap());
//        this.gatewayStrategyRouteEndpoint.modify(gatewayStrategyRouteEntity);
        return Result.ok();
    }

    @PostMapping("delGateway")
    @ResponseBody
    public Result<?> delGateway(@RequestParam(value = "ids") final String ids) {
        final List<String> idList = CommonTool.parseList(ids, ",", String.class);
        for (final String id : idList) {
            this.gatewayStrategyRouteEndpoint.delete(id);
        }
        return Result.ok();
    }

    @PostMapping("enable")
    @ResponseBody
    public Result<?> enable(@RequestParam(value = "id") final String id) {
//        final GatewayStrategyRouteEntity gatewayStrategyRouteEntity = new GatewayStrategyRouteEntity();
//        this.gatewayStrategyRouteEndpoint.add(gatewayStrategyRouteEntity);
        return Result.ok();
    }

    @PostMapping("disable")
    @ResponseBody
    public Result<?> disable(@RequestParam(value = "id") final String id) {
        this.gatewayStrategyRouteEndpoint.delete(id);
        return Result.ok();
    }
//
//    @PostMapping("publish")
//    @ResponseBody
//    public Result<?> publish() throws Exception {
//        this.gatewayRpcService.refresh();
//        return Result.ok();
//    }
}