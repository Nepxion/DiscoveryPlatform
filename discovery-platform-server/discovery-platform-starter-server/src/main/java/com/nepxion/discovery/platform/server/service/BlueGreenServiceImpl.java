package com.nepxion.discovery.platform.server.service;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.platform.server.adapter.PlatformPublishAdapter;
import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import com.nepxion.discovery.platform.server.entity.po.BlueGreenPo;
import com.nepxion.discovery.platform.server.mapper.BlueGreenMapper;

public class BlueGreenServiceImpl extends PlatformPublishAdapter<BlueGreenMapper, BlueGreenDto> implements BlueGreenService {

    @Override
    public IPage<BlueGreenDto> list(String name, Integer page, Integer limit) {
        LambdaQueryWrapper<BlueGreenDto> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like(BlueGreenDto::getDescription, name);
        }
        queryWrapper.orderByAsc(BlueGreenDto::getCreateTime);
        return this.page(new Page<>(page, limit), queryWrapper);
    }

    @Override
    public Boolean insert(BlueGreenPo blueGreenPo) {
        BlueGreenDto blueGreenDto = prepareInsert(new BlueGreenDto());
        blueGreenDto.setPortalName(blueGreenPo.getPortalName());
        blueGreenDto.setPortalType(blueGreenPo.getPortalType());
        blueGreenDto.setType(blueGreenPo.getType());
        blueGreenDto.setStrategy(blueGreenPo.getStrategy());
        blueGreenDto.setCondition(blueGreenPo.getCondition());
        blueGreenDto.setRoute(blueGreenPo.getRoute());
        blueGreenDto.setHeader(blueGreenPo.getHeader());
        blueGreenDto.setDescription(blueGreenPo.getDescription());
        return save(blueGreenDto);
    }
}