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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.dto.SysRoleDto;
import com.nepxion.discovery.platform.server.exception.PlatformException;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlRoleMapper;
import com.nepxion.discovery.platform.server.service.RoleService;

public class MySqlRoleService extends ServiceImpl<MySqlRoleMapper, SysRoleDto> implements RoleService {
    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public List<SysRoleDto> listOrderByName() {
        QueryWrapper<SysRoleDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(SysRoleDto::getName);
        return list(queryWrapper);
    }

    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public List<SysRoleDto> getNotSuperAdmin() {
        QueryWrapper<SysRoleDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysRoleDto::getSuperAdmin, false)
                .orderByAsc(SysRoleDto::getCreateTime);
        return list(queryWrapper);
    }

    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public IPage<SysRoleDto> list(String name, Integer pageNum, Integer pageSize) {
        QueryWrapper<SysRoleDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysRoleDto> sysRoleLambdaQueryWrapper = queryWrapper.lambda().orderByAsc(SysRoleDto::getName);
        if (StringUtils.isNotEmpty(name)) {
            sysRoleLambdaQueryWrapper.like(SysRoleDto::getName, name);
        }
        return page(new Page<>(), queryWrapper);
    }

    @TransactionWriter
    @Override
    public void insert(String name, Boolean superAdmin, String description) {
        SysRoleDto sysRole = getByUserName(name);
        if (sysRole != null) {
            throw new PlatformException(String.format("角色名[%s]已存在", name));
        }
        sysRole = new SysRoleDto();
        sysRole.setName(name);
        sysRole.setSuperAdmin(superAdmin);
        sysRole.setDescription(description);
        save(sysRole);
    }

    @TransactionWriter
    @Override
    public void update(Long id, String name, Boolean superAdmin, String description) {
        if (StringUtils.isEmpty(name) && superAdmin == null && StringUtils.isEmpty(description)) {
            throw new PlatformException("请输入要更新的内容");
        }

        SysRoleDto sysRole = getById(id);
        if (sysRole == null) {
            throw new PlatformException(String.format("角色[%s]不存在", name));
        }

        sysRole.setName(name);
        sysRole.setSuperAdmin(superAdmin);
        sysRole.setDescription(description);
        updateById(sysRole);
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
        return getOne(queryWrapper);
    }
}