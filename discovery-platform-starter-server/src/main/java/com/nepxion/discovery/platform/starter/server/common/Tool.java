package com.nepxion.discovery.platform.starter.server.common;

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
import com.nepxion.discovery.platform.starter.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.starter.server.entity.vo.TimeRange;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;


public final class Tool {
    private static final Logger LOG = LoggerFactory.getLogger(Tool.class);
    private final static String SALT = "PEgASuS";

    public static String getVersion() {
        return System.getProperty("version", PlatformConstant.PLATFORM_VERSION);
    }

    public static String getClientIp(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    public static String hash(final String value) {
        return new Md5Hash(value, SALT).toString();
    }

    public static TimeRange splitTime(String timeRange) {
        if (null == timeRange) {
            return null;
        }
        timeRange = timeRange.trim();
        if (ObjectUtils.isEmpty(timeRange)) {
            return null;
        }
        final TimeRange result = new TimeRange();
        final String[] createTimeRanges = timeRange.split(" - ");
        result.setStart(DateUtil.parse(createTimeRanges[0]));
        result.setEnd(DateUtil.parse(createTimeRanges[1]));
        return result;
    }
}