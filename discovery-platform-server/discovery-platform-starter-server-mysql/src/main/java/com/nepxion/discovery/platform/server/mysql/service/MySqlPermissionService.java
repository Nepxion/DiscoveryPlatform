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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlPermissionMapper;
import com.nepxion.discovery.platform.server.service.PermissionService;

public class MySqlPermissionService extends ServiceImpl<MySqlPermissionMapper, SysPermissionDto> implements PermissionService {
    @TransactionReader
    @Override
    public SysPermissionDto getById(Long id) {
        return super.getById(id);
    }

    @TransactionReader
    @Override
    public List<SysPageDto> listPermissionPagesByRoleId(Long sysRoleId) {
        return baseMapper.listPermissionPagesByRoleId(sysRoleId);
    }

    @TransactionReader
    @Override
    public IPage<PermissionVo> list(Integer pageNum, Integer pageSize, Long sysRoleId, Long sysPageId) {
        return baseMapper.list(new Page<>(pageNum, pageSize), sysRoleId, sysPageId);
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