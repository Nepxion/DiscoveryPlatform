package com.nepxion.discovery.platform.server.ui.interfaces;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.ui.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.server.ui.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.ui.entity.vo.Admin;
import org.apache.ibatis.annotations.Param;

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

public interface AdminService {
    boolean authenticate(final String username,
                         final String password) throws Exception;

    Admin getAdminByUserName(final String username) throws Exception;

    SysAdmin getByUserName(final String username) throws Exception;

    boolean changePassword(final Long id,
                           final String oldPassword,
                           final String newPassword) throws Exception;

    boolean insert(final LoginMode loginMode,
                   final Long roleId,
                   final String username,
                   final String password,
                   final String name,
                   final String phoneNumber,
                   final String email,
                   final String remark) throws Exception;

    boolean update(final Long id,
                   final Long roleId,
                   final String username,
                   final String name,
                   final String phoneNumber,
                   final String email,
                   final String remark) throws Exception;

    IPage<Admin> list(final LoginMode loginMode,
                      final String name,
                      final Integer pageNum,
                      final Integer pageSize) throws Exception;

    List<Admin> search(final String keyword,
                       final Integer pageNum,
                       final Integer pageSize);

    List<SysAdmin> getByRoleId(final Long roleId) throws Exception;

    SysAdmin getById(final Long id);

    boolean removeByIds(@Param("idList") Set<Long> idList);
}
