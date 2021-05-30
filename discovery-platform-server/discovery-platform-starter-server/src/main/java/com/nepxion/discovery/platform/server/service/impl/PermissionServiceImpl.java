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

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.dto.SysMenuDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;
import com.nepxion.discovery.platform.server.mapper.PermissionMapper;
import com.nepxion.discovery.platform.server.service.PermissionService;

public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, SysPermissionDto> implements PermissionService {
    @TransactionReader
    @Override
    public SysPermissionDto getById(Long id) {
        return super.getById(id);
    }

    @TransactionReader
    @Override
    public List<SysMenuDto> listPermissionMenusByRoleId(Long sysRoleId) {
        return baseMapper.listPermissionMenusByRoleId(sysRoleId);
    }

    @TransactionReader
    @Override
    public IPage<PermissionVo> list(Integer pageNum, Integer pageSize, Long sysRoleId, Long sysMenuId) {
        return baseMapper.list(new Page<>(pageNum, pageSize), sysRoleId, sysMenuId);
    }

    @TransactionWriter
    @Override
    public void insert(SysPermissionDto sysPermission) {
        save(sysPermission);
    }

    @TransactionWriter
    @Override
    public boolean updateById(SysPermissionDto entity) {
        return super.updateById(entity);
    }

    @TransactionWriter
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }
}