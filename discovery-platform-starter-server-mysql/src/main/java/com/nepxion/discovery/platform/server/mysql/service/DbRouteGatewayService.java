package com.nepxion.discovery.platform.server.mysql.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.mysql.mapper.DbRouteGatewayMapper;
import com.nepxion.discovery.platform.server.entity.dto.RouteGateway;
import com.nepxion.discovery.platform.server.interfaces.RouteGatewayService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

@Service
public class DbRouteGatewayService extends ServiceImpl<DbRouteGatewayMapper, RouteGateway> implements RouteGatewayService {

    @Override
    public IPage<RouteGateway> page(final String description,
                                    final Integer pageNum,
                                    final Integer pageSize) {
        final QueryWrapper<RouteGateway> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RouteGateway> lambda = queryWrapper.lambda();
        if (!ObjectUtils.isEmpty(description)) {
            lambda.eq(RouteGateway::getDescription, description);
        }
        lambda.orderByDesc(RouteGateway::getRowCreateTime);
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public RouteGateway getById(final Long id) {
        if (id == null) {
            return null;
        }
        return super.getById(id);
    }

    @Override
    public void insert(RouteGateway routeGateway) {
        if (ObjectUtils.isEmpty(routeGateway.getRouteId())) {
            routeGateway.setRouteId("gw_".concat(RandomUtil.randomString(15)));
        }
        this.save(routeGateway);
    }

    @Override
    public void update(RouteGateway routeGateway) {
        this.updateById(routeGateway);
    }

    @Override
    public void delete(final Collection<Long> ids) {
        this.removeByIds(ids);
    }

    @Override
    public void enable(final Long id,
                       final boolean enabled) {
        final UpdateWrapper<RouteGateway> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(RouteGateway::getId, id)
                .set(RouteGateway::getEnabled, enabled);
        this.update(updateWrapper);
    }

    @Override
    public List<RouteGateway> listEnabled() {
        final QueryWrapper<RouteGateway> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RouteGateway::getEnabled, true)
                .orderByAsc(RouteGateway::getOrderNum);
        return this.list(queryWrapper);
    }
}