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

@TableName(value = "`t_blacklist`")
public class BlacklistDto extends BaseStateEntity {
    private static final long serialVersionUID = 7349466331174113582L;

    @TableField(value = "`portal_name`")
    private String portalName;

    @TableField(value = "`portal_type`")
    private Integer portalType;

    @TableField(value = "`service_name`")
    private String serviceName;

    @TableField(value = "`service_blacklist_type`")
    private Integer serviceBlacklistType;

    @TableField(value = "`service_blacklist`")
    private String serviceBlacklist;

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

    public void setPortalType(Integer portalType) {
        this.portalType = portalType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getServiceBlacklistType() {
        return serviceBlacklistType;
    }

    public void setServiceBlacklistType(Integer serviceBlacklistType) {
        this.serviceBlacklistType = serviceBlacklistType;
    }

    public String getServiceBlacklist() {
        return serviceBlacklist;
    }

    public void setServiceBlacklist(String serviceBlacklist) {
        this.serviceBlacklist = serviceBlacklist;
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

    public enum Type {
        UUID(1, "UUID"),
        ADDRESS(2, "ADDRESS");

        private final int code;
        private final String name;

        Type(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static Type get(int code) {
            for (Type item : Type.values()) {
                if (item.getCode() == code) {
                    return item;
                }
            }
            return null;
        }

        public static Type get(String name) {
            for (Type item : Type.values()) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }
    }
}