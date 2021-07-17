package com.nepxion.discovery.platform.server.entity.dto;

import java.util.List;

public class GraphDto {

	private List<GraphNodeDto> nodes;
	private List<GraphEdgeDto> edges;

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
}
