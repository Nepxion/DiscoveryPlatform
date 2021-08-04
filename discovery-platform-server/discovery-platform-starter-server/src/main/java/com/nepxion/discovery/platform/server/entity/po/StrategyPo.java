package com.nepxion.discovery.platform.server.entity.po;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "蓝绿或灰度信息")
public class StrategyPo implements Serializable {
    private static final long serialVersionUID = 7174491897001035436L;
    @ApiModelProperty("蓝绿或灰度的唯一标识id")
    private Long id;

    @ApiModelProperty("入口名称")
    private String portalName;

    @ApiModelProperty("入口类型(1: 网关, 2:服务, 3:组)")
    private Integer portalType;

    @ApiModelProperty("策略类型(1: 版本, 2: 区域)")
    private Integer strategyType;

    @ApiModelProperty("涉及到的所有链路编排标识")
    private String routeIds;

    @ApiModelProperty("用于蓝绿兜底的链路编排标识")
    private String basicBlueGreenStrategyRouteId;

    @ApiModelProperty("蓝绿条件策略")
    private String blueGreenStrategy;

    @ApiModelProperty("用于灰度兜底的信息")
    private String basicGrayStrategy;

    @ApiModelProperty("灰度条件策略")
    private String grayStrategy;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("内置参数")
    private String header;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public Integer getPortalType() {
        return portalType;
    }

    public void setPortalType(Integer portalType) {
        this.portalType = portalType;
    }

    public Integer getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(Integer strategyType) {
        this.strategyType = strategyType;
    }

    public String getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(String routeIds) {
        this.routeIds = routeIds;
    }

    public String getBasicBlueGreenStrategyRouteId() {
        return basicBlueGreenStrategyRouteId;
    }

    public void setBasicBlueGreenStrategyRouteId(String basicBlueGreenStrategyRouteId) {
        this.basicBlueGreenStrategyRouteId = basicBlueGreenStrategyRouteId;
    }

    public String getBlueGreenStrategy() {
        return blueGreenStrategy;
    }

    public void setBlueGreenStrategy(String blueGreenStrategy) {
        this.blueGreenStrategy = blueGreenStrategy;
    }

    public String getBasicGrayStrategy() {
        return basicGrayStrategy;
    }

    public void setBasicGrayStrategy(String basicGrayStrategy) {
        this.basicGrayStrategy = basicGrayStrategy;
    }

    public String getGrayStrategy() {
        return grayStrategy;
    }

    public void setGrayStrategy(String grayStrategy) {
        this.grayStrategy = grayStrategy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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