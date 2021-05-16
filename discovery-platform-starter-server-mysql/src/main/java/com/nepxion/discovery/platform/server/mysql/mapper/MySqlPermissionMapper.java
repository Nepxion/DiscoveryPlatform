package com.nepxion.discovery.platform.server.mysql.mapper;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.dto.SysPermissionDto;
import com.nepxion.discovery.platform.server.entity.vo.PermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MySqlPermissionMapper extends BaseMapper<SysPermissionDto> {
    List<SysPageDto> listPermissionPagesByRoleId(@Param("sysRoleId") Long sysRoleId);

    IPage<PermissionVo> list(IPage<PermissionVo> result,
                             @Param("sysRoleId") Long sysRoleId,
                             @Param("sysPageId") Long sysPageId);
}