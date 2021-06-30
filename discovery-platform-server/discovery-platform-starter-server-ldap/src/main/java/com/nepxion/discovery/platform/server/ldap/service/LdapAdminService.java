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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nepxion.discovery.platform.server.shiro.JwtToolWrapper;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.AuthenticationEntity;
import com.nepxion.discovery.common.entity.UserEntity;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.po.AdminPo;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.LdapUserVo;
import com.nepxion.discovery.platform.server.service.AdminService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdapAdminService implements AdminService {

    public static final Logger LOG = LoggerFactory.getLogger(LdapAdminService.class);

    private LdapService ldapService;
    private AdminService adminService;
    private JwtToolWrapper jwtToolWrapper;

    public LdapAdminService(LdapService ldapService, AdminService adminService) {
        this.ldapService = ldapService;
        this.adminService = adminService;
    }

    @Override
    public void setJwtToolWrapper(JwtToolWrapper jwtToolWrapper) {
        this.jwtToolWrapper = jwtToolWrapper;
    }

    @Override public JwtToolWrapper getJwtToolWrapper() {
        if (null == jwtToolWrapper) {
            throw new NullPointerException("No jwtToolWrapper is set");
        }
        return this.jwtToolWrapper;
    }

    @Override
    public boolean authenticate(String username, String password) {
        return ldapService.authenticate(username, password);
    }

    @Override
    public AdminVo getAdminById(long id) throws Exception {
        return adminService.getAdminById(id);
    }

    @Override
    public AuthenticationEntity authenticate(UserEntity userEntity) {
        AuthenticationEntity authenticationEntity = new AuthenticationEntity();

        String username = userEntity.getUserId();
        String password = userEntity.getPassword();
        AdminVo adminVo;
        try {
            if (!authenticate(username, password)) {
                throw new IncorrectCredentialsException("Password is mismatched");
            }
            adminVo = getAdminByUserName(username);
            if (isSuperAdmin(adminVo.getUsername())) {
                adminVo.getSysRole().setSuperAdmin(true);
            } else {
                adminVo.getSysRole().setSuperAdmin(false);
            }
        } catch (Exception e) {
            String message = ExceptionTool.getRootCauseMessage(e);
            LOG.error(message, e);
            authenticationEntity.setPassed(false);
            authenticationEntity.setError(message);
            return authenticationEntity;
        }
        String token = jwtToolWrapper.generateBearerToken(adminVo);

        authenticationEntity.setPassed(true);
        authenticationEntity.setToken(token);
        return authenticationEntity;
    }

    @Override
    public AdminVo getAdminByUserName(String username) throws Exception {
        LdapUserVo ldapUserVo = ldapService.getByUserName(username);
        if (ldapUserVo == null) {
            return null;
        }
        SysAdminDto sysAdmin = adminService.getByUserName(username);
        AdminPo adminPo = new AdminPo();
        if (sysAdmin == null) {
            adminPo.setRoleId(2L);
            adminPo.setUsername(username);
            adminPo.setPassword(StringUtils.EMPTY);
            adminPo.setName(ldapUserVo.getName());
            adminPo.setPhoneNumber(ldapUserVo.getPhoneNumber());
            adminPo.setEmail(ldapUserVo.getEmail());
            adminPo.setDescription(ldapUserVo.getDescription());
            adminService.insert(LoginMode.LDAP, adminPo);
        } else {
            BeanUtils.copyProperties(adminPo, sysAdmin);
            adminService.update(adminPo);
        }
        return adminService.getAdminByUserName(username);
    }

    @Override
    public List<AdminVo> search(String keyword, Integer pageNum, Integer pageSize) {
        List<AdminVo> result = new ArrayList<>();
        List<LdapUserVo> ldapUserVoList = ldapService.search(keyword, pageNum, pageSize);

        for (LdapUserVo ldapUserVo : ldapUserVoList) {
            AdminVo adminVo = new AdminVo();
            adminVo.setLoginMode(LoginMode.LDAP.getCode());
            adminVo.setUsername(ldapUserVo.getUsername());
            adminVo.setName(ldapUserVo.getName());
            adminVo.setPhoneNumber(ldapUserVo.getPhoneNumber());
            adminVo.setEmail(ldapUserVo.getEmail());
            adminVo.setDescription(ldapUserVo.getDescription());
            result.add(adminVo);
        }
        return result;
    }


    @Override
    public SysAdminDto getByUserName(String username) throws Exception {
        return adminService.getByUserName(username);
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) throws Exception {
        return adminService.changePassword(id, oldPassword, newPassword);
    }

    @Override
    public boolean insert(LoginMode loginMode, AdminPo adminPo) throws Exception {
        return adminService.insert(loginMode, adminPo);
    }

    @Override
    public boolean update(AdminPo adminPo) throws Exception {
        return adminService.update(adminPo);
    }

    @Override
    public IPage<AdminVo> list(LoginMode loginMode, String name, Integer pageNum, Integer pageSize) throws Exception {
        return adminService.list(loginMode, name, pageNum, pageSize);
    }

    @Override
    public List<SysAdminDto> getByRoleId(Long roleId) throws Exception {
        return adminService.getByRoleId(roleId);
    }

    @Override
    public SysAdminDto getById(Long id) {
        return adminService.getById(id);
    }

    @Override
    public boolean removeByIds(Set<Long> idList) {
        return adminService.removeByIds(idList);
    }

    @Override
    public boolean isSuperAdmin(String username) throws Exception {
        return adminService.isSuperAdmin(username);
    }
}