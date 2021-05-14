package com.nepxion.discovery.platform.server.mysql.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.server.mysql.mapper.DbDicMapper;
import com.nepxion.discovery.platform.server.entity.dto.SysDic;
import com.nepxion.discovery.platform.server.interfaces.DicService;
import org.springframework.stereotype.Service;

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
public class DbDicService extends ServiceImpl<DbDicMapper, SysDic> implements DicService {

    @Override
    public String getByName(String name) {
        QueryWrapper<SysDic> queryWrapper = new QueryWrapper<>();
        final SysDic sysDic = this.getOne(queryWrapper);
        if (sysDic == null) {
            return null;
        }
        return sysDic.getValue();
    }
}