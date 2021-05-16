package com.nepxion.discovery.platform.server.mysql.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.common.entity.UserEntity;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlAdminMapper;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.DicService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.anno.TranRead;
import com.nepxion.discovery.platform.server.tool.anno.TranSave;
import com.nepxion.discovery.platform.server.tool.common.CommonTool;
import com.nepxion.discovery.platform.server.tool.exception.BusinessException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MySqlAdminService extends ServiceImpl<MySqlAdminMapper, SysAdminDto> implements AdminService, InitializingBean {
    @Autowired
    private RoleService roleService;
    @Autowired
    private DicService dicService;
    private final Set<String> SUPER_ADMIN_USER_NAME_LIST = new HashSet<>();

    @TranRead
    @Override
    public boolean authenticate(final String username,
                                final String password) {
        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            return false;
        }
        final SysAdminDto sysAdmin = this.getByUserName(username);
        if (null == sysAdmin) {
            return false;
        }

        return CommonTool.hash(password).equals(sysAdmin.getPassword());
    }

    @Override
    public boolean authenticate(UserEntity userEntity) {
        return this.authenticate(userEntity.getUserId(), userEntity.getPassword());
    }

    @TranRead
    @Override
    public AdminVo getAdminByUserName(final String username) throws Exception {
        final SysAdminDto sysAdmin = this.getByUserName(username);
        if (null == sysAdmin) {
            return null;
        }

        final AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(adminVo, sysAdmin);
        adminVo.setSysRole(this.roleService.getById(adminVo.getSysRoleId()));
        return adminVo;
    }

    @TranRead
    @Override
    public SysAdminDto getByUserName(final String username) {
        final QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysAdminDto::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @TranSave
    @Override
    public boolean changePassword(final Long id,
                                  final String oldPassword,
                                  final String newPassword) {
        final SysAdminDto sysAdmin = this.getById(id);
        if (null == sysAdmin) {
            return false;
        }

        if (!sysAdmin.getPassword().equals(CommonTool.hash(oldPassword))) {
            throw new BusinessException("密码不匹配, 修改密码失败");
        }

        UpdateWrapper<SysAdminDto> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysAdminDto::getId, id)
                .set(SysAdminDto::getPassword, CommonTool.hash(newPassword));
        return this.update(updateWrapper);
    }

    @TranSave
    @Override
    public boolean insert(final LoginMode loginMode,
                          final Long roleId,
                          final String username,
                          final String password,
                          final String name,
                          final String phoneNumber,
                          final String email,
                          final String remark) {
        SysAdminDto sysAdmin = this.getByUserName(username);
        if (null != sysAdmin) {
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
    public boolean update(final Long id,
                          Long roleId,
                          String username,
                          String name,
                          String phoneNumber,
                          String email,
                          String remark) {
        final SysAdminDto sysAdmin = this.getById(id);
        if (null == sysAdmin) {
            return false;
        }

        final UpdateWrapper<SysAdminDto> updateWrapper = new UpdateWrapper<>();
        final LambdaUpdateWrapper<SysAdminDto> lambdaUpdateWrapper = updateWrapper.lambda().eq(SysAdminDto::getId, id);

        if (null != roleId) {
            lambdaUpdateWrapper.set(SysAdminDto::getSysRoleId, roleId);
        }
        if (null != username) {
            lambdaUpdateWrapper.set(SysAdminDto::getUsername, username);
        }
        if (null != name) {
            lambdaUpdateWrapper.set(SysAdminDto::getName, name);
        }
        if (null != phoneNumber) {
            lambdaUpdateWrapper.set(SysAdminDto::getPhoneNumber, phoneNumber);
        }
        if (null != email) {
            lambdaUpdateWrapper.set(SysAdminDto::getEmail, email);
        }
        if (null != remark) {
            lambdaUpdateWrapper.set(SysAdminDto::getRemark, remark);
        }
        return this.update(updateWrapper);
    }

    @TranRead
    @Override
    public IPage<AdminVo> list(final LoginMode loginMode,
                               final String name,
                               final Integer pageNum,
                               final Integer pageSize) {
        return this.baseMapper.list(new Page<>(pageNum, pageSize), loginMode.getCode(), name);
    }

    @TranRead
    @Override
    public List<AdminVo> search(final String keyword,
                                final Integer pageNum,
                                final Integer pageSize) {
        final QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(SysAdminDto::getName, keyword);
        final Page<SysAdminDto> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        return CommonTool.toVoList(page.getRecords(), AdminVo.class);
    }

    @TranRead
    @Override
    public List<SysAdminDto> getByRoleId(final Long roleId) {
        final QueryWrapper<SysAdminDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysAdminDto::getSysRoleId, roleId);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public SysAdminDto getById(final Long id) {
        return super.getById(id);
    }

    @TranSave
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }

    @Override
    public boolean isSuperAdmin(final String username) throws Exception {
        boolean result = SUPER_ADMIN_USER_NAME_LIST.contains(username);
        if (!result) {
            result = getAdminByUserName(username).getSysRole().getSuperAdmin();
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() {
        final String value = dicService.getByName(PlatformConstant.SUER_ADMIN_NAME);
        List<String> usernameList = CommonTool.split(value, ",");
        if (!CollectionUtils.isEmpty(usernameList)) {
            SUPER_ADMIN_USER_NAME_LIST.addAll(usernameList);
        }
    }
}