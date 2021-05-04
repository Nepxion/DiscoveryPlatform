package com.nepxion.discovery.platform.server.ineterfaces;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.enums.Mode;

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

public interface AdminService {
    boolean authenticate(final String username,
                         final String password) throws Exception;

    Admin getAdminByUserName(final String username) throws Exception;

    SysAdmin getByUserName(final String username) throws Exception;

    SysAdmin getById(final Long id) throws Exception;

    boolean resetPassword(final Long id) throws Exception;

    boolean changePassword(final Long id,
                           final String oldPassword,
                           final String newPassword) throws Exception;

    void insert(final Mode mode,
                final Long roleId,
                final String username,
                final String password,
                final String name,
                final String phoneNumber,
                final String email,
                final String remark) throws Exception;

    void update(final Long id,
                final Long roleId,
                final String username,
                final String name,
                final String phoneNumber,
                final String email,
                final String remark) throws Exception;

    void removeById(final List<Long> idList) throws Exception;

    IPage<Admin> list(final Mode mode,
                      final String name,
                      final Integer pageNum,
                      final Integer pageSize) throws Exception;

    List<Admin> search(final String keyword,
                       final int offset,
                       final int limit) throws Exception;

    List<SysAdmin> getByRoleId(final Long roleId) throws Exception;
}
