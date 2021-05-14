package com.nepxion.discovery.platform.starter.server.entity.dto;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nepxion.discovery.platform.starter.server.entity.base.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@TableName(value = "`t_route_zuul`")
public final class RouteZuul extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableField(value = "route_id")
    private String routeId;

    @TableField(value = "service_id")
    private String serviceId;

    @TableField(value = "path")
    private String path;

    @TableField(value = "uri")
    private String uri;

    @TableField(value = "strip_prefix")
    private Boolean stripPrefix;

    @TableField(value = "retryable")
    private Boolean retryable;

    @TableField(value = "sensitive_headers")
    private String sensitiveHeaders;

    @TableField(value = "enabled")
    private Boolean enabled;

    @TableField(value = "description")
    private String description;

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