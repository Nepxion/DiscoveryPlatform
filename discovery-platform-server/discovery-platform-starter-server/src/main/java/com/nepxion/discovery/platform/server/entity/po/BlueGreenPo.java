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

@ApiModel(description = "蓝绿路由信息")
public class BlueGreenPo implements Serializable {
    private static final long serialVersionUID = 7174491897001035436L;
    @ApiModelProperty("蓝绿id")
    private Long id;

    @ApiModelProperty("入口类型")
    private Integer portalType;

    @ApiModelProperty("入口名称")
    private String portalName;

    @ApiModelProperty("蓝绿发布策略类型(1:版本策略, 2:区域策略)")
    private Integer type;

    @ApiModelProperty("兜底策略")
    private String basicStrategy;

    @ApiModelProperty("蓝绿条件策略")
    private String blueGreenStrategy;

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

    public Integer getPortalType() {
        return portalType;
    }

    public void setPortalType(Integer portalType) {
        this.portalType = portalType;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBasicStrategy() {
        return basicStrategy;
    }

    public void setBasicStrategy(String basicStrategy) {
        this.basicStrategy = basicStrategy;
    }

    public String getBlueGreenStrategy() {
        return blueGreenStrategy;
    }

    public void setBlueGreenStrategy(String blueGreenStrategy) {
        this.blueGreenStrategy = blueGreenStrategy;
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