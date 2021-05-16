package com.nepxion.discovery.platform.server.ldap.service;

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
import com.nepxion.discovery.common.entity.UserEntity;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.LdapUserVo;
import com.nepxion.discovery.platform.server.service.AdminService;

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
    public boolean authenticate(String username, String password) {
        return this.ldapService.authenticate(username, password);
    }

    @Override
    public boolean authenticate(UserEntity userEntity) {
        return this.authenticate(userEntity.getUserId(), userEntity.getPassword());
    }

    @Override
    public AdminVo getAdminByUserName(String username) throws Exception {
        final LdapUserVo ldapUserVo = this.ldapService.getByUserName(username);
        if (null == ldapUserVo) {
            return null;
        }
        SysAdminDto sysAdmin = this.adminService.getByUserName(username);
        if (null == sysAdmin) {
            this.adminService.insert(LoginMode.LDAP, 2L, username, "", ldapUserVo.getName(), ldapUserVo.getPhoneNumber(), ldapUserVo.getEmail(), ldapUserVo.getRemark());
        } else {
            this.adminService.update(sysAdmin.getId(), sysAdmin.getSysRoleId(), sysAdmin.getUsername(), ldapUserVo.getName(), ldapUserVo.getPhoneNumber(), ldapUserVo.getEmail(), ldapUserVo.getRemark());
        }
        return this.adminService.getAdminByUserName(username);
    }

    @Override
    public List<AdminVo> search(final String keyword,
                                final Integer pageNum,
                                final Integer pageSize) {
        final List<AdminVo> result = new ArrayList<>();
        final List<LdapUserVo> ldapUserVoList = this.ldapService.search(keyword, pageNum, pageSize);

        for (final LdapUserVo ldapUserVo : ldapUserVoList) {
            final AdminVo adminVo = new AdminVo();
            adminVo.setLoginMode(LoginMode.LDAP.getCode());
            adminVo.setUsername(ldapUserVo.getUsername());
            adminVo.setName(ldapUserVo.getName());
            adminVo.setPhoneNumber(ldapUserVo.getPhoneNumber());
            adminVo.setEmail(ldapUserVo.getEmail());
            adminVo.setRemark(ldapUserVo.getRemark());
            result.add(adminVo);
        }
        return result;
    }


    @Override
    public SysAdminDto getByUserName(String username) throws Exception {
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
    public IPage<AdminVo> list(LoginMode loginMode, String name, Integer pageNum, Integer pageSize) throws Exception {
        return this.adminService.list(loginMode, name, pageNum, pageSize);
    }


    @Override
    public List<SysAdminDto> getByRoleId(Long roleId) throws Exception {
        return this.adminService.getByRoleId(roleId);
    }

    @Override
    public SysAdminDto getById(Long id) {
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