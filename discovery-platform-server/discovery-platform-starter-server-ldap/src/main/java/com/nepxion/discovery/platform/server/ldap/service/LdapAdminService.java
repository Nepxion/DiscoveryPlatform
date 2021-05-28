package com.nepxion.discovery.platform.server.ldap.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.AuthenticationEntity;
import com.nepxion.discovery.common.entity.UserEntity;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.LdapUserVo;
import com.nepxion.discovery.platform.server.service.AdminService;

public class LdapAdminService implements AdminService {
    private final LdapService ldapService;
    private final AdminService adminService;

    public LdapAdminService(LdapService ldapService,
                            AdminService adminService) {
        this.ldapService = ldapService;
        this.adminService = adminService;
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.ldapService.authenticate(username, password);
    }

    @Override
    public AuthenticationEntity authenticate(UserEntity userEntity) {
        AuthenticationEntity authenticationEntity = new AuthenticationEntity();
        return authenticationEntity;
    }

    @Override
    public AdminVo getAdminByUserName(String username) throws Exception {
        LdapUserVo ldapUserVo = this.ldapService.getByUserName(username);
        if (ldapUserVo == null) {
            return null;
        }
        SysAdminDto sysAdmin = this.adminService.getByUserName(username);
        if (sysAdmin == null) {
            this.adminService.insert(LoginMode.LDAP, 2L, username, "", ldapUserVo.getName(), ldapUserVo.getPhoneNumber(), ldapUserVo.getEmail(), ldapUserVo.getRemark());
        } else {
            this.adminService.update(sysAdmin.getId(), sysAdmin.getSysRoleId(), sysAdmin.getUsername(), ldapUserVo.getName(), ldapUserVo.getPhoneNumber(), ldapUserVo.getEmail(), ldapUserVo.getRemark());
        }
        return this.adminService.getAdminByUserName(username);
    }

    @Override
    public List<AdminVo> search(String keyword,
                                Integer pageNum,
                                Integer pageSize) {
        List<AdminVo> result = new ArrayList<>();
        List<LdapUserVo> ldapUserVoList = this.ldapService.search(keyword, pageNum, pageSize);

        for (LdapUserVo ldapUserVo : ldapUserVoList) {
            AdminVo adminVo = new AdminVo();
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