package com.nepxion.discovery.platform.server.common;

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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.vo.TimeRange;
import com.nepxion.discovery.platform.tool.db.DataSourceTool;
import com.nepxion.discovery.platform.tool.exception.ExceptionTool;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


public final class Tool {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceTool.class);
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

    public static <T> T toVo(final Object source,
                             final Class<T> target) {
        if (null == source) {
            return null;
        }
        try {
            final T result = target.newInstance();
            BeanUtils.copyProperties(source, result);
            return result;
        } catch (final Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return null;
        }
    }

    public static <T> List<T> toVoList(final List<?> source,
                                       final Class<T> target) {
        if (null == source) {
            return null;
        }
        try {
            final List<T> result = new ArrayList<>(source.size());
            for (final Object o : source) {
                result.add(toVo(o, target));
            }
            return result;
        } catch (final Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return null;
        }
    }

    public static String hash(final String value) {
        return new Md5Hash(value, SALT).toString();
    }

    public static String toPrettyFormatJson(final String json) {
        final JSONValidator.Type type = JSONValidator.from(json).getType();
        if (JSONValidator.Type.Array == type) {
            return JSON.toJSONString(JSONArray.parse(json),
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.QuoteFieldNames,
                    SerializerFeature.WriteBigDecimalAsPlain);
        } else if (null == type || JSONValidator.Type.Object == type) {
            return JSON.toJSONString(JSONObject.parseObject(json),
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.QuoteFieldNames,
                    SerializerFeature.WriteBigDecimalAsPlain);
        } else {
            return json;
        }
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