package com.nepxion.discovery.platform.server.services.memory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPermission;
import com.nepxion.discovery.platform.server.entity.vo.Permission;
import com.nepxion.discovery.platform.server.ineterfaces.PermissionService;

import java.util.List;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public class MemoryPermissionService implements PermissionService {

    @Override
    public List<SysPage> listPermissionPagesByRoleId(Long sysRoleId) {
        return null;
    }

    @Override
    public IPage<Permission> list(Integer pageNum, Integer pageSize, Long sysRoleId, Long sysPageId) {
        return null;
    }

    @Override
    public void insert(SysPermission sysPermission) {

    }

    @Override
    public SysPermission getById(Long id) {
        return null;
    }

    @Override
    public void updateById(SysPermission sysPermission) {

    }

    @Override
    public void removeByIds(List<Long> idsList) {

    }
}
