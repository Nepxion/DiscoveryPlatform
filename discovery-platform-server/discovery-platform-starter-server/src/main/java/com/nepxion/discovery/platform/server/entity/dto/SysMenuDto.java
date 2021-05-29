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
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;

@TableName(value = "`sys_menu`")
public class SysMenuDto extends BaseEntity {
    private static final long serialVersionUID = -3106165704196966179L;

    /**
     * 页面名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 页面地址
     */
    @TableField(value = "`url`")
    private String url;

    /**
     * 页面是否出现在菜单栏
     */
    @TableField(value = "`show_flag`")
    private Boolean showFlag;

    /**
     * 是否是默认页(只允许有一个默认页，如果设置多个，以第一个为准)
     */
    @TableField(value = "`default_flag`")
    private Boolean defaultFlag;

    /**
     * 是否新开窗口打开页面
     */
    @TableField(value = "`blank_flag`")
    private Boolean blankFlag;

    /**
     * html中的图标样式
     */
    @TableField(value = "`icon_class`")
    private String iconClass;

    /**
     * 父级id(即本表的主键id)
     */
    @TableField(value = "`parent_id`")
    private Long parentId;

    /**
     * 顺序号(值越小, 排名越靠前)
     */
    @TableField(value = "`order`")
    private Long order;

    /**
     * 菜单描述信息
     */
    @TableField(value = "`description`")
    private String description;

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