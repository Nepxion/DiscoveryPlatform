package com.nepxion.discovery.platform.starter.server.ldap.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.starter.server.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.starter.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.starter.server.entity.vo.Admin;
import com.nepxion.discovery.platform.starter.server.entity.vo.LdapUser;
import com.nepxion.discovery.platform.starter.server.interfaces.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LdapAdminService implements AdminService {
    private final LdapService ldapService;
    private final AdminService adminService;

    public LdapAdminService(final LdapService ldapService,
                            final AdminService adminService) {
        this.ldapService = ldapService;
        this.adminService = adminService;
    }

    @Override
    public boolean authenticate(String username, String password) throws Exception {
        return this.ldapService.authenticate(username, password);
    }

    @Override
    public Admin getAdminByUserName(String username) throws Exception {
        final LdapUser ldapUser = this.ldapService.getByUserName(username);
        if (null == ldapUser) {
            return null;
        }
        SysAdmin sysAdmin = this.adminService.getByUserName(username);
        if (null == sysAdmin) {
            this.adminService.insert(LoginMode.LDAP, 2L, username, "", ldapUser.getName(), ldapUser.getPhoneNumber(), ldapUser.getEmail(), ldapUser.getRemark());
        } else {
            this.adminService.update(sysAdmin.getId(), sysAdmin.getSysRoleId(), sysAdmin.getUsername(), ldapUser.getName(), ldapUser.getPhoneNumber(), ldapUser.getEmail(), ldapUser.getRemark());
        }
        return this.adminService.getAdminByUserName(username);
    }

    @Override
    public List<Admin> search(final String keyword,
                              final Integer pageNum,
                              final Integer pageSize) {
        final List<Admin> result = new ArrayList<>();
        final List<LdapUser> ldapUsersList = this.ldapService.search(keyword, pageNum, pageSize);

        for (final LdapUser ldapUser : ldapUsersList) {
            final Admin admin = new Admin();
            admin.setLoginMode(LoginMode.LDAP.getCode());
            admin.setUsername(ldapUser.getUsername());
            admin.setName(ldapUser.getName());
            admin.setPhoneNumber(ldapUser.getPhoneNumber());
            admin.setEmail(ldapUser.getEmail());
            admin.setRemark(ldapUser.getRemark());
            result.add(admin);
        }

        return result;
    }


    @Override
    public SysAdmin getByUserName(String username) throws Exception {
        return this.adminService.getByUserName(username);
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) throws Exception {
        return this.adminService.changePassword(id, oldPassword, newPassword);
    }

    @Override
    public boolean insert(LoginMode loginMode, Long roleId, String username, String password, String name, String phoneNumber, String email, String remark) throws Exception {
        return this.adminService.insert(loginMode, roleId, username, password, name, phoneNumber, email, remark);
    }

    @Override
    public boolean update(Long id, Long roleId, String username, String name, String phoneNumber, String email, String remark) throws Exception {
        return this.adminService.update(id, roleId, username, name, phoneNumber, email, remark);
    }

    @Override
    public IPage<Admin> list(LoginMode loginMode, String name, Integer pageNum, Integer pageSize) throws Exception {
        return this.adminService.list(loginMode, name, pageNum, pageSize);
    }


    @Override
    public List<SysAdmin> getByRoleId(Long roleId) throws Exception {
        return this.adminService.getByRoleId(roleId);
    }

    @Override
    public SysAdmin getById(Long id) {
        return this.adminService.getById(id);
    }

    @Override
    public boolean removeByIds(Set<Long> idList) {
        return this.adminService.removeByIds(idList);
    }

    @Override
    public boolean isSuperAdmin(String username) throws Exception {
        return this.adminService.isSuperAdmin(username);
    }
}