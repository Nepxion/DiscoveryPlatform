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

@TableName(value = "`t_blue_green`")
public class BlueGreenDto extends BaseStateEntity {
    private static final long serialVersionUID = 7349466331174113582L;

    @TableField(value = "`type`")
    private Integer type;

    @TableField(value = "`basic_strategy`")
    private String basicStrategy;

    @TableField(value = "`blue_green_strategy`")
    private String blueGreenStrategy;

    @TableField(value = "`header`")
    private String header;

    @TableField(value = "`description`")
    private String description;

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

    public enum Type {
        VERSION(1, "version strategy"),
        REGION(2, "region strategy");

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