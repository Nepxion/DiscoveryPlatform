package com.nepxion.discovery.platform.server.service;

import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import com.nepxion.discovery.platform.server.service.base.BasePublishService;

public interface GrayService extends BasePublishService<BlueGreenDto> {
	String getGraySetting(Long id);
}
