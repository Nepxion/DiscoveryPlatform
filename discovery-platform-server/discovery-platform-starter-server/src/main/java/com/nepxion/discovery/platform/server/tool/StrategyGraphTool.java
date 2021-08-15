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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyReleaseEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.common.util.ReflectionUtil;
import com.nepxion.discovery.platform.server.entity.dto.GraphDto;
import com.nepxion.discovery.platform.server.entity.dto.GraphEdgeDto;
import com.nepxion.discovery.platform.server.entity.dto.GraphNodeDto;
import com.nepxion.discovery.platform.server.entity.enums.GraphNodeType;
import com.nepxion.discovery.platform.server.entity.vo.InstanceMetaVo;

public class StrategyGraphTool {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyGraphTool.class);
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

    // ==================================== Inspector ====================================
    public static GraphDto convertInspectResultToGraph(String dimension, Integer requestCounts,
                                                       List<List<InstanceMetaVo>> chainCountingList) {
        GraphDto graphDto = new GraphDto();
        if (CollectionUtils.isEmpty(chainCountingList) || CollectionUtils.isEmpty(chainCountingList.get(0))) {
            return graphDto;
        }
        List<GraphNodeDto> nodes = new ArrayList<>();
        List<GraphEdgeDto> edges = new ArrayList<>();
        // Init root node
        InstanceMetaVo rootInstanceMeta = chainCountingList.get(0).get(0);
        rootInstanceMeta.setNodeType(GraphNodeType.ROOT);
        rootInstanceMeta.setCount(requestCounts);
        GraphNodeDto rootNode = instanceMetaToNode(dimension, rootInstanceMeta);
        nodes.add(rootNode);

        Map<String, GraphNodeDto> instanceNodeMap = new LinkedHashMap<>();
        Map<String, GraphEdgeDto> instanceEdgeMap = new LinkedHashMap<>();
        for (List<InstanceMetaVo> instanceMetaList : chainCountingList) {
            List<GraphNodeDto> routeNodes = new ArrayList<>();
            routeNodes.clear();
            instanceMetaList.remove(0);
            instanceMetaList.forEach(instanceMeta -> {
                GraphNodeDto node = instanceMetaToNode(dimension, instanceMeta);
                routeNodes.add(node);
            });
            List<GraphEdgeDto> routeEdges = buildRouteEdges(rootNode, routeNodes, nodes, instanceNodeMap);
            buildTracingRoute(routeEdges, edges, instanceEdgeMap);
        }

        graphDto.setNodes(nodes);
        graphDto.setEdges(edges);

        return graphDto;
    }

    private static List<GraphEdgeDto> buildRouteEdges(GraphNodeDto rootNode, List<GraphNodeDto> routeNodes,
                                                      List<GraphNodeDto> nodes, Map<String, GraphNodeDto> instanceNodeMap) {
        List<GraphEdgeDto> routeEdges = new ArrayList<>();
        String sourceId = rootNode.getId();
        for (GraphNodeDto routeNode : routeNodes) {
            String routeNodeId = refreshNodeValue(routeNode, nodes, instanceNodeMap);

            GraphEdgeDto edge = new GraphEdgeDto();
            edge.setSource(sourceId);
            edge.setTarget(routeNodeId);
            edge.setCount(routeNode.getCount());

            routeEdges.add(edge);
            sourceId = routeNodeId;
        }
        return routeEdges;
    }

    private static String refreshNodeValue(GraphNodeDto routeNode, List<GraphNodeDto> nodes, Map<String, GraphNodeDto> instanceNodeMap) {
        String routeNodeId = routeNode.getId();
        GraphNodeDto node = instanceNodeMap.get(routeNodeId);
        if (Objects.nonNull(node)) {
            Integer count = node.getCount() + routeNode.getCount();
            node.setCount(count);
            return routeNodeId;
        }
        instanceNodeMap.put(routeNodeId, routeNode);
        nodes.add(routeNode);

        return routeNodeId;
    }

    private static void buildTracingRoute(List<GraphEdgeDto> routeEdges, List<GraphEdgeDto> edges, Map<String, GraphEdgeDto> instanceEdgeMap) {
        for (GraphEdgeDto routeEdge : routeEdges) {
            String route = routeEdge.getSource() + "-" + routeEdge.getTarget();
            GraphEdgeDto edge = instanceEdgeMap.get(route);
            if (Objects.nonNull(edge)) {
                Integer count = edge.getCount() + routeEdge.getCount();
                edge.setCount(count);
                continue;
            }
            instanceEdgeMap.put(route, routeEdge);
            edges.add(routeEdge);
        }
    }

    private static GraphNodeDto instanceMetaToNode(String dimension, InstanceMetaVo instanceMeta) {
        GraphNodeType nodeType = instanceMeta.getNodeType();
        if (Objects.isNull(nodeType)) {
            nodeType = GraphNodeType.NORMAL;
        }
        GraphNodeDto graphNodeDto = new GraphNodeDto();
        graphNodeDto.setId(instanceMeta.getHost());
        graphNodeDto.setLabel(instanceMeta.getServiceId());
        graphNodeDto.setType(nodeType.getType());
        graphNodeDto.setValue(instanceMeta.getHost());
        graphNodeDto.setCount(instanceMeta.getCount());
        graphNodeDto.setDimension(dimension);
        graphNodeDto.setDimensionValue(getDimensionValue(dimension, instanceMeta));
        return graphNodeDto;
    }

    public static String getDimensionValue(String dimension, InstanceMetaVo instanceMeta) {
        try {
            return (String) ReflectionUtil.getValue(InstanceMetaVo.class, instanceMeta, dimension);
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e));
        }
        return null;
    }
}