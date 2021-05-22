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
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MySqlRouteGatewayMapper extends BaseMapper<RouteGatewayDto> {

}