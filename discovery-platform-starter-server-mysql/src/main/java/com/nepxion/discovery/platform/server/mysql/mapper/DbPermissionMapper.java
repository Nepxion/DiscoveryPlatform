package com.nepxion.discovery.platform.server.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPermission;
import com.nepxion.discovery.platform.server.entity.vo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

@Mapper
public interface DbPermissionMapper extends BaseMapper<SysPermission> {
    List<SysPage> listPermissionPagesByRoleId(@Param("sysRoleId") Long sysRoleId);

    IPage<Permission> list(IPage<Permission> result,
                           @Param("sysRoleId") Long sysRoleId,
                           @Param("sysPageId") Long sysPageId);
}