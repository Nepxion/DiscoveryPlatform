package com.nepxion.discovery.platform.server.ineterfaces;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPermission;
import com.nepxion.discovery.platform.server.entity.vo.Permission;

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

public interface PermissionService {
    List<SysPage> listPermissionPagesByRoleId(final Long sysRoleId) throws Exception;

    IPage<Permission> list(final Integer pageNum,
                           final Integer pageSize,
                           final Long sysRoleId,
                           final Long sysPageId) throws Exception;

    void insert(final SysPermission sysPermission) throws Exception;

    SysPermission getById(final Long id) throws Exception;

    void updateById(final SysPermission sysPermission) throws Exception;

    void removeByIds(final List<Long> idsList) throws Exception;
}
