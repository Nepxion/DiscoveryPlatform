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
import com.nepxion.discovery.platform.server.entity.dto.SysRoleDto;

public class AdminVo extends BaseEntity {
    private static final long serialVersionUID = -6127678430528273318L;

    @TableField(value = "login_mode")
    private Integer loginMode;

    @TableField(value = "`sys_role_id`")
    private Long sysRoleId;

    @TableField(value = "`role_name`")
    private String roleName;

    @TableField(value = "`username`")
    private String username;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "`phone_number`")
    private String phoneNumber;

    @TableField(value = "`email`")
    private String email;

    @TableField(value = "`description`")
    private String description;

    @TableField(exist = false)
    private String defaultPage;

    @TableField(exist = false)
    private SysRoleDto sysRole;

    @TableField(exist = false)
    private List<MenuVo> permissions;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(String defaultPage) {
        this.defaultPage = defaultPage;
    }

    public SysRoleDto getSysRole() {
        return sysRole;
    }

    public void setSysRole(SysRoleDto sysRole) {
        this.sysRole = sysRole;
    }

    public List<MenuVo> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<MenuVo> permissions) {
        this.permissions = permissions;
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