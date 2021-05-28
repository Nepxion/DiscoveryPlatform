package com.nepxion.discovery.platform.server.mysql.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
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
import com.nepxion.discovery.platform.server.annotation.TranRead;
import com.nepxion.discovery.platform.server.annotation.TranSave;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.exception.BusinessException;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlAdminMapper;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.DicService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

public class MySqlAdminService extends ServiceImpl<MySqlAdminMapper, SysAdminDto> implements AdminService, InitializingBean {
    @Autowired
    private RoleService roleService;
    @Autowired
    private DicService dicService;
    private static final Set<String> SUPER_ADMIN_USER_NAME_LIST = new HashSet<>();

    @TranRead
    @Override
    public boolean authenticate(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }
        SysAdminDto sysAdmin = this.getByUserName(username);
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

    @TranRead
    @Override
    public AdminVo getAdminByUserName(String username) throws Exception {
        SysAdminDto sysAdmin = this.getByUserName(username);
        if (sysAdmin == null) {
            return null;
        }

        AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(adminVo, sysAdmin);
        adminVo.setSysRole(this.roleService.getById(adminVo.getSysRoleId()));
        return adminVo;
    }

    @TranRead
    @Override
    public SysAdminDto getByUserName(String username) {
        QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysAdminDto::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @TranSave
    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        SysAdminDto sysAdmin = this.getById(id);
        if (sysAdmin == null) {
            return false;
        }

        if (!sysAdmin.getPassword().equals(oldPassword)) {
            throw new BusinessException("密码不匹配, 修改密码失败");
        }

        UpdateWrapper<SysAdminDto> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysAdminDto::getId, id)
                .set(SysAdminDto::getPassword, newPassword);
        return this.update(updateWrapper);
    }

    @TranSave
    @Override
    public boolean insert(LoginMode loginMode,
                          Long roleId,
                          String username,
                          String password,
                          String name,
                          String phoneNumber,
                          String email,
                          String remark) {
        SysAdminDto sysAdmin = this.getByUserName(username);
        if (sysAdmin != null) {
            throw new BusinessException(String.format("用户名[%s]已存在", username));
        }

        sysAdmin = new SysAdminDto();
        sysAdmin.setLoginMode(loginMode.getCode());
        sysAdmin.setSysRoleId(roleId);
        sysAdmin.setUsername(username);
        sysAdmin.setPassword(CommonTool.hash(password));
        sysAdmin.setName(name);
        sysAdmin.setPhoneNumber(phoneNumber);
        sysAdmin.setEmail(email);
        sysAdmin.setRemark(remark);
        return this.save(sysAdmin);
    }

    @TranSave
    @Override
    public boolean update(Long id,
                          Long roleId,
                          String username,
                          String name,
                          String phoneNumber,
                          String email,
                          String remark) {
        SysAdminDto sysAdmin = this.getById(id);
        if (sysAdmin == null) {
            return false;
        }

        UpdateWrapper<SysAdminDto> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<SysAdminDto> lambdaUpdateWrapper = updateWrapper.lambda().eq(SysAdminDto::getId, id);

        if (roleId != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getSysRoleId, roleId);
        }
        if (username != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getUsername, username);
        }
        if (name != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getName, name);
        }
        if (phoneNumber != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getPhoneNumber, phoneNumber);
        }
        if (email != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getEmail, email);
        }
        if (remark != null) {
            lambdaUpdateWrapper.set(SysAdminDto::getRemark, remark);
        }
        return this.update(updateWrapper);
    }

    @TranRead
    @Override
    public IPage<AdminVo> list(LoginMode loginMode, String name, Integer pageNum, Integer pageSize) {
        return this.baseMapper.list(new Page<>(pageNum, pageSize), loginMode.getCode(), name);
    }

    @TranRead
    @Override
    public List<AdminVo> search(String keyword, Integer pageNum, Integer pageSize) {
        QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(SysAdminDto::getName, keyword);
        Page<SysAdminDto> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        return CommonTool.toVoList(page.getRecords(), AdminVo.class);
    }

    @TranRead
    @Override
    public List<SysAdminDto> getByRoleId(Long roleId) {
        QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAdminDto::getSysRoleId, roleId);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public SysAdminDto getById(Long id) {
        return super.getById(id);
    }

    @TranSave
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