package com.nepxion.discovery.platform.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysRoleDto;

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

public interface RoleService {
    List<SysRoleDto> listOrderByName() throws Exception;

    List<SysRoleDto> getNotSuperAdmin() throws Exception;

    IPage<SysRoleDto> list(final String name,
                           final Integer pageNum,
                           final Integer pageSize) throws Exception;

    void insert(final String name,
                final Boolean superAdmin,
                final String remark) throws Exception;

    void update(final Long id,
                final String name,
                final Boolean superAdmin,
                final String remark) throws Exception;

    SysRoleDto getById(Long sysRoleId);

    boolean removeByIds(Set<Long> idSet);
}