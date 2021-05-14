package com.nepxion.discovery.platform.starter.server.entity.dto;

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
import com.nepxion.discovery.platform.starter.server.entity.base.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@TableName(value = "`sys_admin`")
public final class SysAdmin extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 角色类型(1: DB 2: LDAP)
     */
    @TableField(value = "login_mode")
    private Integer loginMode;

    /**
     * 角色id(sys_role表的主键)
     */
    @TableField(value = "`sys_role_id`")
    private Long sysRoleId;

    /**
     * 管理员的登陆用户名
     */
    @TableField(value = "`username`")
    private String username;

    /**
     * 管理员的登陆密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 管理员姓名
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 手机号码
     */
    @TableField(value = "`phone_number`")
    private String phoneNumber;

    /**
     * 邮件地址
     */
    @TableField(value = "`email`")
    private String email;

    /**
     * 备注
     */
    @TableField(value = "`remark`")
    private String remark;

    public Integer getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(Integer loginMode) {
        this.loginMode = loginMode;
    }

    public Long getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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