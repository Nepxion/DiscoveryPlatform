package com.nepxion.discovery.platform.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public interface BlueGreenService {
    IPage<BlueGreenDto> list(String name, Integer page, Integer limit);
}