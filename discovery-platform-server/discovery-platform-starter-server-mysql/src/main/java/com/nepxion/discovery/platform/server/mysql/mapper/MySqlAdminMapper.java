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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;

@Mapper
public interface MySqlAdminMapper extends BaseMapper<SysAdminDto> {
    IPage<AdminVo> list(IPage<AdminVo> page, @Param("mode") int mode, @Param("name") String name);
}