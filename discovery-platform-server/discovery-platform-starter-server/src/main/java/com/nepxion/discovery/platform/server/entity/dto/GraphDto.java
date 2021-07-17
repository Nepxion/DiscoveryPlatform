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
