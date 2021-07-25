package com.nepxion.discovery.platform.server.tool;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Xuehui Ren
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyReleaseEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.entity.dto.GraphDto;
import com.nepxion.discovery.platform.server.entity.dto.GraphEdgeDto;
import com.nepxion.discovery.platform.server.entity.dto.GraphNodeDto;

public class StrategyGraphTool {
    private static final String PORTAL_NODE_ID = "portal";

    private static List<GraphNodeDto> initAllGraphNodes(String portalName, List<StrategyRouteEntity> strategyRouteEntityList) {
        List<GraphNodeDto> nodes = new ArrayList<>();
        GraphNodeDto portal = new GraphNodeDto();
        portal.setType(PORTAL_NODE_ID);
        portal.setId(PORTAL_NODE_ID);
        portal.setLabel(portalName);
        nodes.add(portal);

        for (StrategyRouteEntity route : strategyRouteEntityList) {
            Map<String, String> serverWithVersion = JsonUtil.fromJson(route.getValue(), Map.class);

            boolean first = true;
            for (Entry<String, String> server : serverWithVersion.entrySet()) {
                GraphNodeDto node = new GraphNodeDto();
                node.setId(route.getId() + "_" + server.getKey());
                node.setLabel(server.getKey());
                node.setValue(server.getValue());
                node.setRouteId(route.getId());

                if (first) {
                    node.setFirstInRoute(true);
                    first = false;
                }

                nodes.add(node);
            }
        }
        return nodes;
    }

    public static GraphDto convertRuleEntityToGraph(String portalName,
                                                    StrategyReleaseEntity strategyReleaseEntity) {
        GraphDto graphDto = new GraphDto();
        if (strategyReleaseEntity == null) {
            return graphDto;
        }

        List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = strategyReleaseEntity.getStrategyConditionBlueGreenEntityList();
        List<StrategyRouteEntity> strategyRouteEntityList = strategyReleaseEntity.getStrategyRouteEntityList();
        List<GraphNodeDto> nodes = initAllGraphNodes(portalName, strategyRouteEntityList);

        List<GraphEdgeDto> edges = new ArrayList<>();
        for (StrategyRouteEntity route : strategyRouteEntityList) {
            graphDto.getRouteCondition().put(route.getId(),
                    getRouteExpression(route.getId(), strategyConditionBlueGreenEntityList));
            initRouteEdges(
                    nodes.stream().filter(node ->
                            route.getId().equals(node.getRouteId())
                    ).collect(Collectors.toList()),
                    edges,
                    route.getId());
        }

        graphDto.setNodes(nodes);
        graphDto.setEdges(edges);

        return graphDto;
    }

    private static String getRouteExpression(String routeId, List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList) {
        Optional<StrategyConditionBlueGreenEntity> condition
                = strategyConditionBlueGreenEntityList.stream().filter(
                entity-> routeId.equals(entity.getRegionId())
                        || routeId.equals(entity.getVersionId())
                        || routeId.equals(entity.getAddressId())
        ).findFirst();
        return condition.isPresent() ? condition.get().getExpression() : "";
    }

    private static void initRouteEdges(List<GraphNodeDto> nodes,
                                       List<GraphEdgeDto> edges,
                                       String routeId) {

        String sourceId = PORTAL_NODE_ID;
        for (GraphNodeDto node : nodes) {
            GraphEdgeDto edge = new GraphEdgeDto();
            edge.setSource(sourceId);
            edge.setTarget(node.getId());
            edge.setRouteId(routeId);

            edges.add(edge);
            sourceId = node.getId();
        }
    }
}