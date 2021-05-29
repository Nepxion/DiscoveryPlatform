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

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;

public interface PermissionService {
    List<SysPageDto> listPermissionPagesByRoleId(Long sysRoleId) throws Exception;

    IPage<PermissionVo> list(Integer pageNum, Integer pageSize, Long sysRoleId, Long sysPageId) throws Exception;

    void insert(SysPermissionDto authPermission);

    SysPermissionDto getById(Long id);

    boolean updateById(SysPermissionDto sysPermission);

    boolean removeByIds(Set<Long> idList);
}