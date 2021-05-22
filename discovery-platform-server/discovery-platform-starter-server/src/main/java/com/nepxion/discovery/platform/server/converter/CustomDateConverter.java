package com.nepxion.discovery.platform.server.converter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import cn.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class CustomDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(final String source) {
        return DateUtil.parse(source);
    }
}