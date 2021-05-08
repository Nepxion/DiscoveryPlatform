package com.nepxion.discovery.platform.mysql.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.mysql.mapper.DbPermissionMapper;
import com.nepxion.discovery.platform.server.ui.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.ui.entity.dto.SysPermission;
import com.nepxion.discovery.platform.server.ui.entity.vo.Permission;
import com.nepxion.discovery.platform.server.ui.interfaces.PermissionService;
import com.nepxion.discovery.platform.server.ui.tool.anno.TranRead;
import com.nepxion.discovery.platform.server.ui.tool.anno.TranSave;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

@Service
public class DbPermissionService extends ServiceImpl<DbPermissionMapper, SysPermission> implements PermissionService {

    @TranRead
    @Override
    public SysPermission getById(Long id) {
        return super.getById(id);
    }

    @TranRead
    @Override
    public List<SysPage> listPermissionPagesByRoleId(final Long sysRoleId) {
        return this.baseMapper.listPermissionPagesByRoleId(sysRoleId);
    }

    @TranRead
    @Override
    public IPage<Permission> list(final Integer pageNum,
                                  final Integer pageSize,
                                  final Long sysRoleId,
                                  final Long sysPageId) {
        return this.baseMapper.list(new Page<>(pageNum, pageSize), sysRoleId, sysPageId);
    }

    @TranSave
    @Override
    public void insert(final SysPermission sysPermission) {
        this.save(sysPermission);
    }

    @TranSave
    @Override
    public boolean updateById(SysPermission entity) {
        return super.updateById(entity);
    }

    @TranSave
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }
}
