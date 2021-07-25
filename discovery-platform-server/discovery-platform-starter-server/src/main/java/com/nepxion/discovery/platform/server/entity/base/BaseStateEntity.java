package com.nepxion.discovery.platform.server.entity.base;

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

public class BaseStateEntity extends BaseEntity {
    private static final long serialVersionUID = 2636994153216571320L;

    @TableField(value = "`operation`")
    private Integer operation;

    @TableField(value = "`enable_flag`")
    private Boolean enableFlag;

    @TableField(value = "`publish_flag`")
    private Boolean publishFlag = false;

    @TableField(value = "`delete_flag`")
    private Boolean deleteFlag = false;

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public Boolean getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Boolean getPublishFlag() {
        return publishFlag;
    }

    public void setPublishFlag(Boolean publishFlag) {
        this.publishFlag = publishFlag;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public enum PortalType {
        GATEWAY(1, "网关"),
        SERVICE(2, "服务"),
        GROUP(3, "组");

        private final int code;
        private final String name;

        PortalType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static PortalType get(int code) {
            for (PortalType item : PortalType.values()) {
                if (item.getCode() == code) {
                    return item;
                }
            }
            return null;
        }

        public static PortalType get(String name) {
            for (PortalType item : PortalType.values()) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }
    }
}