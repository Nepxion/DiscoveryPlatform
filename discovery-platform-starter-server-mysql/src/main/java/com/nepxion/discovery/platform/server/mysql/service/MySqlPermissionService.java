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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.annotation.TranRead;
import com.nepxion.discovery.platform.server.annotation.TranSave;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlPermissionMapper;
import com.nepxion.discovery.platform.server.service.PermissionService;

import java.util.List;
import java.util.Set;

public class MySqlPermissionService extends ServiceImpl<MySqlPermissionMapper, SysPermissionDto> implements PermissionService {
    @TranRead
    @Override
    public SysPermissionDto getById(Long id) {
        return super.getById(id);
    }

    @TranRead
    @Override
    public List<SysPageDto> listPermissionPagesByRoleId(final Long sysRoleId) {
        return this.baseMapper.listPermissionPagesByRoleId(sysRoleId);
    }

    @TranRead
    @Override
    public IPage<PermissionVo> list(final Integer pageNum,
                                    final Integer pageSize,
                                    final Long sysRoleId,
                                    final Long sysPageId) {
        return this.baseMapper.list(new Page<>(pageNum, pageSize), sysRoleId, sysPageId);
    }

    @TranSave
    @Override
    public void insert(final SysPermissionDto sysPermission) {
        this.save(sysPermission);
    }

    @TranSave
    @Override
    public boolean updateById(SysPermissionDto entity) {
        return super.updateById(entity);
    }

    @TranSave
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }
}