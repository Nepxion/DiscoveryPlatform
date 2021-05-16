package com.nepxion.discovery.platform.server.mysql.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.entity.dto.RouteZuulDto;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlRouteZuulMapper;
import com.nepxion.discovery.platform.server.service.RouteZuulService;
import org.springframework.stereotype.Service;

public class MySqlRouteZuulService extends ServiceImpl<MySqlRouteZuulMapper, RouteZuulDto> implements RouteZuulService {

}