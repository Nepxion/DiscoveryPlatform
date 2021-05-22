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
import com.nepxion.discovery.platform.server.entity.vo.PageVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MySqlPageMapper extends BaseMapper<SysPageDto> {
    IPage<PageVo> list(IPage<PageVo> result,
                       @Param("name") String name);

    Long getMaxOrder(@Param("parentId") Long parentId);

    List<PageVo> listPermissionPages(@Param("adminId") Long adminId);
}