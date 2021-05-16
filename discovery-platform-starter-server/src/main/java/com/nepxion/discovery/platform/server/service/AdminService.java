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
import com.nepxion.discovery.console.resource.AuthenticationResource;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface AdminService extends AuthenticationResource {
    boolean authenticate(final String username,
                         final String password) throws Exception;

    AdminVo getAdminByUserName(final String username) throws Exception;

    SysAdminDto getByUserName(final String username) throws Exception;

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

    IPage<AdminVo> list(final LoginMode loginMode,
                        final String name,
                        final Integer pageNum,
                        final Integer pageSize) throws Exception;

    List<AdminVo> search(final String keyword,
                         final Integer pageNum,
                         final Integer pageSize);

    List<SysAdminDto> getByRoleId(final Long roleId) throws Exception;

    SysAdminDto getById(final Long id);

    boolean removeByIds(@Param("idList") Set<Long> idList);

    boolean isSuperAdmin(final String username) throws Exception;
}
