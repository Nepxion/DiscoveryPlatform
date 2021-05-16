package com.nepxion.discovery.platform.server.service;


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
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;

import java.util.List;
import java.util.Set;

public interface PermissionService {
    List<SysPageDto> listPermissionPagesByRoleId(final Long sysRoleId) throws Exception;

    IPage<PermissionVo> list(final Integer pageNum,
                             final Integer pageSize,
                             final Long sysRoleId,
                             final Long sysPageId) throws Exception;

    void insert(final SysPermissionDto authPermission);

    SysPermissionDto getById(final Long id);

    boolean updateById(final SysPermissionDto sysPermission);

    boolean removeByIds(final Set<Long> idList);
}
