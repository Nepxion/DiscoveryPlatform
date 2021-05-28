package com.nepxion.discovery.platform.server.mysql.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.List;
import java.util.Set;

import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.annotation.TranRead;
import com.nepxion.discovery.platform.server.annotation.TranSave;
import com.nepxion.discovery.platform.server.entity.dto.SysRoleDto;
import com.nepxion.discovery.platform.server.exception.BusinessException;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlRoleMapper;
import com.nepxion.discovery.platform.server.service.RoleService;

public class MySqlRoleService extends ServiceImpl<MySqlRoleMapper, SysRoleDto> implements RoleService {
    @TranRead
    @Override
    public List<SysRoleDto> listOrderByName() {
        QueryWrapper<SysRoleDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(SysRoleDto::getName);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public List<SysRoleDto> getNotSuperAdmin() {
        QueryWrapper<SysRoleDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysRoleDto::getSuperAdmin, false)
                .orderByAsc(SysRoleDto::getRowCreateTime);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public IPage<SysRoleDto> list(String name, Integer pageNum, Integer pageSize) {
        QueryWrapper<SysRoleDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysRoleDto> sysRoleLambdaQueryWrapper = queryWrapper.lambda().orderByAsc(SysRoleDto::getName);
        if (!ObjectUtils.isEmpty(name)) {
            sysRoleLambdaQueryWrapper.like(SysRoleDto::getName, name);
        }
        return this.page(new Page<>(), queryWrapper);
    }

    @TranSave
    @Override
    public void insert(String name, Boolean superAdmin, String remark) {
        SysRoleDto sysRole = this.getByUserName(name);
        if (sysRole != null) {
            throw new BusinessException(String.format("角色名[%s]已存在", name));
        }
        sysRole = new SysRoleDto();
        sysRole.setName(name);
        sysRole.setSuperAdmin(superAdmin);
        sysRole.setRemark(remark);
        this.save(sysRole);
    }

    @TranSave
    @Override
    public void update(Long id,
                       String name,
                       Boolean superAdmin,
                       String remark) {
        if (ObjectUtils.isEmpty(name) && superAdmin == null && ObjectUtils.isEmpty(remark)) {
            throw new BusinessException("请输入要更新的内容");
        }

        SysRoleDto sysRole = this.getById(id);
        if (sysRole == null) {
            throw new BusinessException(String.format("角色[%s]不存在", name));
        }

        sysRole.setName(name);
        sysRole.setSuperAdmin(superAdmin);
        sysRole.setRemark(remark);
        this.updateById(sysRole);
    }

    @Override
    public SysRoleDto getById(Long sysRoleId) {
        return super.getById(sysRoleId);
    }

    @Override
    public boolean removeByIds(Set<Long> idSet) {
        return super.removeByIds(idSet);
    }

    private SysRoleDto getByUserName(String name) {
        QueryWrapper<SysRoleDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleDto::getName, name);
        return this.getOne(queryWrapper);
    }
}