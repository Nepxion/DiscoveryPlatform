package com.nepxion.discovery.platform.server.entity.base;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -6126908183507715467L;

    /**
     * 自增主键
     */
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    /**
     * 记录创建时间
     */
    @TableField(value = "`row_create_time`")
    private Date rowCreateTime;

    /**
     * 最后一次修改时间
     */
    @TableField(value = "`row_update_time`")
    private Date rowUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRowCreateTime() {
        return rowCreateTime;
    }

    public void setRowCreateTime(Date rowCreateTime) {
        this.rowCreateTime = rowCreateTime;
    }

    public Date getRowUpdateTime() {
        return rowUpdateTime;
    }

    public void setRowUpdateTime(Date rowUpdateTime) {
        this.rowUpdateTime = rowUpdateTime;
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