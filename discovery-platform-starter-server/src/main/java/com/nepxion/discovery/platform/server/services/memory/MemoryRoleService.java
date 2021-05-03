package com.nepxion.discovery.platform.server.services.memory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysRole;
import com.nepxion.discovery.platform.server.ineterfaces.RoleService;

import java.util.Collection;
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

public class MemoryRoleService implements RoleService {

    @Override
    public List<SysRole> listOrderByName() {
        return null;
    }

    @Override
    public List<SysRole> getNotSuperAdmin() {
        return null;
    }

    @Override
    public IPage<SysRole> list(String name, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public void insert(String name, Boolean superAdmin, String remark) {

    }

    @Override
    public void update(Long id, String name, Boolean superAdmin, String remark) {

    }

    @Override
    public SysRole getById(Long sysRoleId) {
        return null;
    }

    @Override
    public void removeById(Collection<Long> ids) {

    }
}
