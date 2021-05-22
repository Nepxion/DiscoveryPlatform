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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nepxion.discovery.platform.server.entity.base.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@TableName(value = "`sys_permission`")
public final class SysPermissionDto extends BaseEntity {
    private static final long serialVersionUID = -1492837850971982832L;

    @TableField(value = "`sys_role_id`")
    private Long sysRoleId;

    @TableField(value = "`sys_page_id`")
    private Long sysPageId;

    @TableField(value = "`can_insert`")
    private Boolean canInsert;

    @TableField(value = "`can_delete`")
    private Boolean canDelete;

    @TableField(value = "`can_update`")
    private Boolean canUpdate;

    @TableField(value = "`can_select`")
    private Boolean canSelect;

    public Long getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public Long getSysPageId() {
        return sysPageId;
    }

    public void setSysPageId(Long sysPageId) {
        this.sysPageId = sysPageId;
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