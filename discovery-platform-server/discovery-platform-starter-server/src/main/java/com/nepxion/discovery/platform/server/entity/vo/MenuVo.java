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

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.TableField;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;

public class MenuVo extends BaseEntity {
    private static final long serialVersionUID = -4924651888194918771L;

    @TableField("`name`")
    private String name;

    @TableField("`url`")
    private String url;

    @TableField("`menu_flag`")
    private Boolean menuFlag;

    @TableField(value = "`default_flag`")
    private Boolean defaultFlag;

    @TableField(value = "`blank_flag`")
    private Boolean blankFlag;

    @TableField("`icon_class`")
    private String iconClass;

    @TableField("`parent_id`")
    private Long parentId;

    @TableField("`parent_name`")
    private String parentName;

    @TableField("`order`")
    private Long order;

    @TableField("`remark`")
    private String remark;

    @TableField("`can_insert`")
    private Boolean canInsert;

    @TableField("`can_delete`")
    private Boolean canDelete;

    @TableField("`can_update`")
    private Boolean canUpdate;

    @TableField("`can_select`")
    private Boolean canSelect;

    @TableField(exist = false)
    private List<MenuVo> children;

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

    public Boolean getMenuFlag() {
        return menuFlag;
    }

    public void setMenuFlag(Boolean menuFlag) {
        this.menuFlag = menuFlag;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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

    public Boolean getCanInsert() {
        return canInsert;
    }

    public void setCanInsert(Boolean canInsert) {
        this.canInsert = canInsert;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getCanSelect() {
        return canSelect;
    }

    public void setCanSelect(Boolean canSelect) {
        this.canSelect = canSelect;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
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