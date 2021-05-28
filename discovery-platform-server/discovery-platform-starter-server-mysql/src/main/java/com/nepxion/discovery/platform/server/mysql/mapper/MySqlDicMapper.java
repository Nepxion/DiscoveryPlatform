package com.nepxion.discovery.platform.server.mysql.mapper;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nepxion.discovery.platform.server.entity.dto.SysDicDto;

@Mapper
public interface MySqlDicMapper extends BaseMapper<SysDicDto> {

}