package com.nepxion.discovery.platform.server.entity.dto;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nepxion.discovery.platform.server.entity.base.BaseStateEntity;

@TableName(value = "`t_route_gateway`")
public class RouteGatewayDto extends BaseStateEntity {
    private static final long serialVersionUID = 2636994152216571320L;

    @TableField(value = "`route_id`")
    private String routeId;

    @TableField(value = "`gateway_name`")
    private String gatewayName;

    @TableField(value = "`uri`")
    private String uri;

    @TableField(value = "`predicates`")
    private String predicates;

    @TableField(value = "`user_predicates`")
    private String userPredicates;

    @TableField(value = "`filters`")
    private String filters;

    @TableField(value = "`user_filters`")
    private String userFilters;

    @TableField(value = "`metadata`")
    private String metadata;

    @TableField(value = "`order`")
    private Integer order;

    @TableField(value = "`service_name`")
    private String serviceName;

    @TableField(value = "`description`")
    private String description;


    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPredicates() {
        return predicates;
    }

    public void setPredicates(String predicates) {
        this.predicates = predicates;
    }

    public String getUserPredicates() {
        return userPredicates;
    }

    public void setUserPredicates(String userPredicates) {
        this.userPredicates = userPredicates;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getUserFilters() {
        return userFilters;
    }

    public void setUserFilters(String userFilters) {
        this.userFilters = userFilters;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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