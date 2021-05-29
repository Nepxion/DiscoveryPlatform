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

@ApiModel(description = "菜单信息")
public class MenuPo implements Serializable {
    private static final long serialVersionUID = -3106165704196966179L;

    @ApiModelProperty("菜单id")
    private Long id;

    @ApiModelProperty("页面名称")
    private String name;

    @ApiModelProperty("页面地址")
    private String url;

    @ApiModelProperty("页面是否出现在菜单栏")
    private Boolean showFlag;

    @ApiModelProperty("是否是默认页(只允许有一个默认页，如果设置多个，以第一个为准)")
    private Boolean defaultFlag;

    @ApiModelProperty("是否新开窗口打开页面")
    private Boolean blankFlag;

    @ApiModelProperty("html中的图标样式")
    private String iconClass;

    @ApiModelProperty("父级id(即本表的主键id)")
    private Long parentId;

    @ApiModelProperty("顺序号(值越小, 排名越靠前)")
    private Long order;

    @ApiModelProperty("备注")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Boolean showFlag) {
        this.showFlag = showFlag;
    }

    public Boolean getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Boolean getBlankFlag() {
        return blankFlag;
    }

    public void setBlankFlag(Boolean blankFlag) {
        this.blankFlag = blankFlag;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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