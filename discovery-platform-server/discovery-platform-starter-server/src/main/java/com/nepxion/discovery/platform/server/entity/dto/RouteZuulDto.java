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

@TableName(value = "`t_route_zuul`")
public class RouteZuulDto extends BaseStateEntity {
    private static final long serialVersionUID = -3893202134019770579L;

    @TableField(value = "`route_id`")
    private String routeId;

    @TableField(value = "`service_id`")
    private String serviceId;

    @TableField(value = "`path`")
    private String path;

    @TableField(value = "`url`")
    private String url;

    @TableField(value = "`strip_prefix`")
    private Boolean stripPrefix;

    @TableField(value = "`retryable`")
    private Boolean retryable;

    @TableField(value = "`sensitive_headers`")
    private String sensitiveHeaders;

    @TableField(value = "`custom_sensitive_headers`")
    private Boolean customSensitiveHeaders;

    @TableField(value = "`description`")
    private String description;

    @TableField(value = "`create_times_in_day`")
    private Integer createTimesInDay;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(Boolean stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public String getSensitiveHeaders() {
        return sensitiveHeaders;
    }

    public void setSensitiveHeaders(String sensitiveHeaders) {
        this.sensitiveHeaders = sensitiveHeaders;
    }

    public Boolean getCustomSensitiveHeaders() {
        return customSensitiveHeaders;
    }

    public void setCustomSensitiveHeaders(Boolean customSensitiveHeaders) {
        this.customSensitiveHeaders = customSensitiveHeaders;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreateTimesInDay() {
        return createTimesInDay;
    }

    public void setCreateTimesInDay(Integer createTimesInDay) {
        this.createTimesInDay = createTimesInDay;
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