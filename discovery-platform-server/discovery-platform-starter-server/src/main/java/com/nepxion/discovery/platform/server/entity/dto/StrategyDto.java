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

@TableName(value = "`t_strategy`")
public class StrategyDto extends BaseStateEntity {
    private static final long serialVersionUID = 7349466331174113582L;

    @TableField(value = "`portal_name`")
    private String portalName;

    @TableField(value = "`portal_type`")
    private Integer portalType;

    @TableField(value = "`strategy_type`")
    private Integer strategyType;

    @TableField(value = "`basic_blue_green_strategy_route_id`")
    private String basicBlueGreenStrategyRouteId;

    @TableField(value = "`blue_green_strategy`")
    private String blueGreenStrategy;

    @TableField(value = "`basic_gray_strategy`")
    private String basicGrayStrategy;

    @TableField(value = "`gray_strategy`")
    private String grayStrategy;

    @TableField(value = "`header`")
    private String header;

    @TableField(value = "`description`")
    private String description;

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public Integer getPortalType() {
        return portalType;
    }

    public Integer getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(Integer strategyType) {
        this.strategyType = strategyType;
    }

    public void setPortalType(Integer portalType) {
        this.portalType = portalType;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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