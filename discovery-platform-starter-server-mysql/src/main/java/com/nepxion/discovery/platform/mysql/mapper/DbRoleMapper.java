package com.nepxion.discovery.platform.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nepxion.discovery.platform.server.ui.entity.dto.SysRole;
import org.apache.ibatis.annotations.Mapper;

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
public interface DbRoleMapper extends BaseMapper<SysRole> {

}