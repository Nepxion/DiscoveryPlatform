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

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.console.resource.AuthenticationResource;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;

public interface AdminService extends AuthenticationResource {
    boolean authenticate(String username, String password) throws Exception;

    AdminVo getAdminByUserName(String username) throws Exception;

    SysAdminDto getByUserName(String username) throws Exception;

    boolean changePassword(Long id, String oldPassword, String newPassword) throws Exception;

    boolean insert(LoginMode loginMode,
                   Long roleId,
                   String username,
                   String password,
                   String name,
                   String phoneNumber,
                   String email,
                   String remark) throws Exception;

    boolean update(Long id,
                   Long roleId,
                   String username,
                   String name,
                   String phoneNumber,
                   String email,
                   String remark) throws Exception;

    IPage<AdminVo> list(LoginMode loginMode, String name, Integer pageNum, Integer pageSize) throws Exception;

    List<AdminVo> search(String keyword, Integer pageNum, Integer pageSize);

    List<SysAdminDto> getByRoleId(Long roleId) throws Exception;

    SysAdminDto getById(Long id);

    boolean removeByIds(@Param("idList") Set<Long> idList);

    boolean isSuperAdmin(String username) throws Exception;
}