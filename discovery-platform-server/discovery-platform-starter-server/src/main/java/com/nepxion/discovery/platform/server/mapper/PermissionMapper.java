package com.nepxion.discovery.platform.server.mapper;

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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysMenuDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;

@Mapper
public interface PermissionMapper extends BaseMapper<SysPermissionDto> {
    List<SysMenuDto> listPermissionMenusByRoleId(@Param("sysRoleId") Long sysRoleId);

    IPage<PermissionVo> list(IPage<PermissionVo> result, @Param("sysRoleId") Long sysRoleId, @Param("sysMenuId") Long sysMenuId);
}