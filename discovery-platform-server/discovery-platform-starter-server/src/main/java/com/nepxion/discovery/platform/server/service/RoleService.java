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
import com.nepxion.discovery.platform.server.entity.dto.SysRoleDto;

public interface RoleService {
    List<SysRoleDto> listOrderByName() throws Exception;

    List<SysRoleDto> getNotSuperAdmin() throws Exception;

    IPage<SysRoleDto> list(String name, Integer pageNum, Integer pageSize) throws Exception;

    void insert(String name, Boolean superAdmin, String description) throws Exception;

    void update(Long id, String name, Boolean superAdmin, String description) throws Exception;

    SysRoleDto getById(Long sysRoleId);

    boolean removeByIds(Set<Long> idSet);
}