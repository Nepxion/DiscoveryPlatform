package com.nepxion.discovery.platform.server.entity.dto;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Xuehui Ren
 * @version 1.0
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GraphDto implements Serializable {
    private static final long serialVersionUID = -3557880595989048996L;
    private List<GraphNodeDto> nodes;
    private List<GraphEdgeDto> edges;
    private Map<String, String> routeCondition = new HashMap<>();

    public List<GraphNodeDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNodeDto> nodes) {
        this.nodes = nodes;
    }

    public List<GraphEdgeDto> getEdges() {
        return edges;
    }

    public void setEdges(List<GraphEdgeDto> edges) {
        this.edges = edges;
    }

    public Map<String, String> getRouteCondition() {
        return routeCondition;
    }

    public void setRouteCondition(Map<String, String> routeCondition) {
        this.routeCondition = routeCondition;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}