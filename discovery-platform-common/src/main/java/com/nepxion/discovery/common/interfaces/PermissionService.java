package com.nepxion.discovery.common.interfaces;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.dto.SysPage;
import com.nepxion.discovery.common.entity.dto.SysPermission;
import com.nepxion.discovery.common.entity.vo.Permission;

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

public interface PermissionService {
    List<SysPage> listPermissionPagesByRoleId(final Long sysRoleId) throws Exception;

    IPage<Permission> list(final Integer pageNum,
                           final Integer pageSize,
                           final Long sysRoleId,
                           final Long sysPageId) throws Exception;

    void insert(final SysPermission authPermission);

    SysPermission getById(final Long id);

    boolean updateById(final SysPermission sysPermission);

    boolean removeByIds(final Set<Long> idList);
}
