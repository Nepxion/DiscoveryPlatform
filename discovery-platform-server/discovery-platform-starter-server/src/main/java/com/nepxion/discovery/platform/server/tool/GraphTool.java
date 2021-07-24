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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyReleaseEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.entity.dto.GraphDto;
import com.nepxion.discovery.platform.server.entity.dto.GraphEdgeDto;
import com.nepxion.discovery.platform.server.entity.dto.GraphNodeDto;

public class GraphTool {
    private static final String BEGIN_NODE_ID = "begin";

    @SuppressWarnings("unchecked")
    private static Map<String, GraphNodeDto> initServerNodeCache(StrategyRouteEntity route) {
        Map<String, GraphNodeDto> nodeCache = new HashMap<>();
        Map<String, String> serverWithVersion = JsonUtil.fromJson(route.getValue(), Map.class);
        for (Entry<String, String> server : serverWithVersion.entrySet()) {
            GraphNodeDto node = new GraphNodeDto();
            node.setId(route.getId() + "_" + server.getKey());
            node.setLabel(server.getKey());
            node.setValue(server.getValue());
            nodeCache.put(route.getId() + "_" + server.getKey(), node);
        }
        return nodeCache;
    }

    public static GraphDto convertRuleEntityToGraph(String portalName, RuleEntity ruleEntity) {
        GraphDto graphDto = new GraphDto();

        StrategyReleaseEntity strategyReleaseEntity = ruleEntity.getStrategyReleaseEntity();
        if (strategyReleaseEntity != null) {
            List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = strategyReleaseEntity.getStrategyConditionBlueGreenEntityList();
            List<StrategyRouteEntity> strategyRouteEntityList = strategyReleaseEntity.getStrategyRouteEntityList();

            Map<String, StrategyConditionBlueGreenEntity> routeWithCondition = new HashMap<>();
            for (StrategyConditionBlueGreenEntity condition : strategyConditionBlueGreenEntityList) {
                routeWithCondition.put(condition.getVersionId(), condition);
            }

            List<GraphNodeDto> nodes = new ArrayList<>();
            GraphNodeDto begin = new GraphNodeDto();
            begin.setType(BEGIN_NODE_ID);
            begin.setId(BEGIN_NODE_ID);
            begin.setLabel(portalName);
            nodes.add(begin);

            List<GraphEdgeDto> edges = new ArrayList<>();
            for (StrategyRouteEntity route : strategyRouteEntityList) {
                Map<String, GraphNodeDto> nodeCache = initServerNodeCache(route);
                nodes.addAll(nodeCache.values());
                String sourceId = BEGIN_NODE_ID;
                boolean first = true;
                StrategyConditionBlueGreenEntity condition = routeWithCondition.get(route.getId());
                for (GraphNodeDto node : nodeCache.values()) {
                    GraphEdgeDto edge = new GraphEdgeDto();
                    edge.setSource(sourceId);
                    edge.setTarget(node.getId());
                    if (first) {
                        node.setRouteId(route.getId());
                        node.setCondition(condition.getExpression());
                    }

                    edges.add(edge);
                    first = false;
                    sourceId = node.getId();
                }
            }

            graphDto.setNodes(nodes);
            graphDto.setEdges(edges);
        }
        return graphDto;
    }
}