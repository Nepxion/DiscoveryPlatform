package com.nepxion.discovery.platform.server.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.entity.dto.SysDicDto;
import com.nepxion.discovery.platform.server.mapper.DicMapper;

public class DicServiceImpl extends ServiceImpl<DicMapper, SysDicDto> implements DicService {
    @Override
    public String getByName(String name) {
        QueryWrapper<SysDicDto> queryWrapper = new QueryWrapper<>();
        SysDicDto sysDic = getOne(queryWrapper);
        if (sysDic == null) {
            return null;
        }
        return sysDic.getValue();
    }
}