package com.nepxion.discovery.platform.server.service.impl;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.entity.AuthenticationEntity;
import com.nepxion.discovery.common.entity.UserEntity;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.po.AdminPo;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.exception.PlatformException;
import com.nepxion.discovery.platform.server.mapper.MySqlAdminMapper;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.DicService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

public class AdminServiceImpl extends ServiceImpl<MySqlAdminMapper, SysAdminDto> implements AdminService, InitializingBean {
    @Autowired
    private RoleService roleService;

    @Autowired
    private DicService dicService;

    private static final Set<String> SUPER_ADMIN_USER_NAME_LIST = new HashSet<>();

    @TransactionReader
    @Override
    public boolean authenticate(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }
        SysAdminDto sysAdmin = getByUserName(username);
        if (sysAdmin == null) {
            return false;
        }

        return CommonTool.hash(password).equals(sysAdmin.getPassword());
    }

    @Override
    public AuthenticationEntity authenticate(UserEntity userEntity) {
        AuthenticationEntity authenticationEntity = new AuthenticationEntity();
        return authenticationEntity;
    }

    @TransactionReader
    @Override
    public AdminVo getAdminByUserName(String username) throws Exception {
        SysAdminDto sysAdmin = getByUserName(username);
        if (sysAdmin == null) {
            return null;
        }

        AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(adminVo, sysAdmin);
        adminVo.setSysRole(roleService.getById(adminVo.getSysRoleId()));
        return adminVo;
    }

    @TransactionReader
    @Override
    public SysAdminDto getByUserName(String username) {
        QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysAdminDto::getUsername, username);
        return getOne(queryWrapper);
    }

    @TransactionWriter
    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        SysAdminDto sysAdmin = getById(id);
        if (sysAdmin == null) {
            return false;
        }

        if (!sysAdmin.getPassword().equals(oldPassword)) {
            throw new PlatformException("密码不匹配, 修改密码失败");
        }

        UpdateWrapper<SysAdminDto> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysAdminDto::getId, id)
                .set(SysAdminDto::getPassword, newPassword);
        return update(updateWrapper);
    }

    @TransactionWriter
    @Override
    public boolean insert(LoginMode loginMode, AdminPo adminPo) {
        SysAdminDto sysAdmin = getByUserName(adminPo.getUsername());
        if (sysAdmin != null) {
            throw new PlatformException(String.format("用户名[%s]已存在", adminPo.getUsername()));
        }

        sysAdmin = new SysAdminDto();
        sysAdmin.setLoginMode(loginMode.getCode());
        sysAdmin.setSysRoleId(adminPo.getRoleId());
        sysAdmin.setUsername(adminPo.getUsername());
        sysAdmin.setPassword(CommonTool.hash(adminPo.getPassword()));
        sysAdmin.setName(adminPo.getName());
        sysAdmin.setPhoneNumber(adminPo.getPhoneNumber());
        sysAdmin.setEmail(adminPo.getEmail());
        sysAdmin.setDescription(adminPo.getDescription());
        return save(sysAdmin);
    }

    @TransactionWriter
    @Override
    public boolean update(AdminPo adminPo) {
        SysAdminDto sysAdmin = getById(adminPo.getId());
        if (sysAdmin == null) {
            return false;
        }

        UpdateWrapper<SysAdminDto> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<SysAdminDto> lambdaUpdateWrapper = updateWrapper.lambda().eq(SysAdminDto::getId, adminPo.getId());

        if (adminPo.getRoleId() != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getSysRoleId, adminPo.getRoleId());
        }
        if (adminPo.getUsername() != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getUsername, adminPo.getUsername());
        }
        if (adminPo.getName() != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getName, adminPo.getName());
        }
        if (adminPo.getPhoneNumber() != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getPhoneNumber, adminPo.getPhoneNumber());
        }
        if (adminPo.getEmail() != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getEmail, adminPo.getEmail());
        }
        if (adminPo.getDescription() != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getDescription, adminPo.getDescription());
        }
        return update(updateWrapper);
    }

    @TransactionReader
    @Override
    public IPage<AdminVo> list(LoginMode loginMode, String name, Integer pageNum, Integer pageSize) {
        return baseMapper.list(new Page<>(pageNum, pageSize), loginMode.getCode(), name);
    }

    @TransactionReader
    @Override
    public List<AdminVo> search(String keyword, Integer pageNum, Integer pageSize) {
        QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(SysAdminDto::getName, keyword);
        Page<SysAdminDto> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        return CommonTool.toVoList(page.getRecords(), AdminVo.class);
    }

    @TransactionReader
    @Override
    public List<SysAdminDto> getByRoleId(Long roleId) {
        QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAdminDto::getSysRoleId, roleId);
        return list(queryWrapper);
    }

    @TransactionReader
    @Override
    public SysAdminDto getById(Long id) {
        return super.getById(id);
    }

    @TransactionWriter
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }

    @Override
    public boolean isSuperAdmin(String username) throws Exception {
        boolean result = SUPER_ADMIN_USER_NAME_LIST.contains(username);
        if (!result) {
            result = getAdminByUserName(username).getSysRole().getSuperAdmin();
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() {
        String value = dicService.getByName(PlatformConstant.SUER_ADMIN_NAME);
        List<String> usernameList = CommonTool.split(value, ",");
        if (!CollectionUtils.isEmpty(usernameList)) {
            SUPER_ADMIN_USER_NAME_LIST.addAll(usernameList);
        }
    }
}