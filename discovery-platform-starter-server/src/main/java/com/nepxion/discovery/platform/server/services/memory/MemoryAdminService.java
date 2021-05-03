package com.nepxion.discovery.platform.server.services.memory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.enums.Mode;
import com.nepxion.discovery.platform.server.ineterfaces.AdminService;

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

public class MemoryAdminService implements AdminService {


    @Override
    public boolean authenticate(String username, String password) throws Exception {
        return false;
    }

    @Override
    public Admin getAdminByUserName(String username) throws Exception {
        return null;
    }

    @Override
    public SysAdmin getByUserName(String username) throws Exception {
        return null;
    }

    @Override
    public SysAdmin getById(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean resetPassword(Long id) throws Exception {
        return false;
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) throws Exception {
        return false;
    }

    @Override
    public void insert(Mode mode, Long roleId, String username, String password, String name, String phoneNumber, String email, String remark) throws Exception {

    }

    @Override
    public void update(Long id, Long roleId, String username, String name, String phoneNumber, String email, String remark) throws Exception {

    }

    @Override
    public void removeById(List<Long> idList) throws Exception {

    }

    @Override
    public IPage<Admin> list(Mode mode, String name, Integer pageNum, Integer pageSize) throws Exception {
        return null;
    }

    @Override
    public List<Admin> search(String keyword, int offset, int limit) throws Exception {
        return null;
    }

    @Override
    public List<SysAdmin> getByRoleId(Long roleId) throws Exception {
        return null;
    }
}
