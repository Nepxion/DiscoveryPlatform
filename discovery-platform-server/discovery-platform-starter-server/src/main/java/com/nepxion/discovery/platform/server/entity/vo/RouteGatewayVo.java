package com.nepxion.discovery.platform.server.entity.vo;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.entity.po.RouteGatewayPo;

public class RouteGatewayVo implements Serializable {
    private static final long serialVersionUID = -730031129510373510L;

    private String host;
    private String port;
    private List<RouteGatewayPoVo> routes;

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

    public List<RouteGatewayPoVo> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteGatewayPoVo> routes) {
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

    public static class RouteGatewayPoVo extends RouteGatewayPo {
        private static final long serialVersionUID = 2600817965821067952L;

        public String getUserPredicatesJson() {
            return getClauseJson(getUserPredicates());
        }

        public String getUserFiltersJson() {
            return getClauseJson(getUserFilters());
        }

        private <T extends Clause> String getClauseJson(List<T> clauseList) {
            StringBuilder userFilterStringBuilder = new StringBuilder();

            for (Clause clause : clauseList) {
                userFilterStringBuilder.append(String.format("%s=%s, ", clause.getName(), JsonUtil.toJson(clause.getArgs())));
            }

            if (userFilterStringBuilder.length() > 0) {
                userFilterStringBuilder.delete(userFilterStringBuilder.length() - 2, userFilterStringBuilder.length());
            }

            return userFilterStringBuilder.toString();
        }
    }
}