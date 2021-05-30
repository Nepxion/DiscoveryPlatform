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
import com.nepxion.discovery.platform.server.entity.vo.MenuVo;

@Mapper
public interface MySqlMenuMapper extends BaseMapper<SysMenuDto> {
    IPage<MenuVo> list(IPage<MenuVo> menuVoIPage, @Param("name") String name);

    Long getMaxOrder(@Param("parentId") Long parentId);

    List<MenuVo> listPermissionMenus(@Param("adminId") Long adminId);
}