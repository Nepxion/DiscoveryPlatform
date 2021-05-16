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

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.entity.dto.RouteGatewayDto;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlRouteGatewayMapper;
import com.nepxion.discovery.platform.server.service.RouteGatewayService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

@Service
public class MySqlRouteGatewayService extends ServiceImpl<MySqlRouteGatewayMapper, RouteGatewayDto> implements RouteGatewayService {

    @Override
    public IPage<RouteGatewayDto> page(final String description,
                                       final Integer pageNum,
                                       final Integer pageSize) {
        final QueryWrapper<RouteGatewayDto> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteGatewayDto> lambda = queryWrapper.lambda();
        if (!ObjectUtils.isEmpty(description)) {
            lambda.eq(RouteGatewayDto::getDescription, description);
        }
        lambda.orderByDesc(RouteGatewayDto::getRowCreateTime);
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public RouteGatewayDto getById(final Long id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

    @Override
    public void insert(RouteGatewayDto routeGateway) {
        if (ObjectUtils.isEmpty(routeGateway.getRouteId())) {
            routeGateway.setRouteId("gw_".concat(RandomUtil.randomString(15)));
        }
        this.save(routeGateway);
    }

    @Override
    public void update(RouteGatewayDto routeGateway) {
        this.updateById(routeGateway);
    }

    @Override
    public void delete(final Collection<Long> ids) {
        this.removeByIds(ids);
    }

    @Override
    public void enable(final Long id,
                       final boolean enabled) {
        final UpdateWrapper<RouteGatewayDto> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(RouteGatewayDto::getId, id)
                .set(RouteGatewayDto::getEnabled, enabled);
        this.update(updateWrapper);
    }

    @Override
    public List<RouteGatewayDto> listEnabled() {
        final QueryWrapper<RouteGatewayDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RouteGatewayDto::getEnabled, true)
                .orderByAsc(RouteGatewayDto::getOrder);
        return this.list(queryWrapper);
    }
}