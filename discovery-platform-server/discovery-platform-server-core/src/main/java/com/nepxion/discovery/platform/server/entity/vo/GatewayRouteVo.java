package com.nepxion.discovery.platform.server.entity.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.nepxion.discovery.platform.server.entity.po.RouteGatewayPo;

import java.util.List;

public class GatewayRouteVo {
    private String host;
    private String port;
    private List<RouteGatewayPo> routes;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<RouteGatewayPo> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteGatewayPo> routes) {
        this.routes = routes;
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