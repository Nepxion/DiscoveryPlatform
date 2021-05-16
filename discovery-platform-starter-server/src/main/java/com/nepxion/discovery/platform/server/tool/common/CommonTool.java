package com.nepxion.discovery.platform.server.tool.common;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.tool.exception.ExceptionTool;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommonTool {
    private static final Logger LOG = LoggerFactory.getLogger(CommonTool.class);
    private final static String SALT = "PEgASuS";


    public static String getVersion() {
        return System.getProperty("version", PlatformConstant.PLATFORM_VERSION);
    }

    public static String hash(final String value) {
        return new Md5Hash(value, SALT).toString();
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


    public static <T> List<T> parseList(final String value,
                                        final String separate,
                                        final Class<T> tClass) {
        final String[] array = value.split(separate);
        List<T> result = new ArrayList<>(array.length);
        for (final String item : array) {
            if (ObjectUtils.isEmpty(item)) {
                continue;
            }
            result.add((T) ConvertUtils.convert(item, tClass));
        }
        return result;
    }

    public static <T> List<T> split(final String value,
                                    final String separator,
                                    final Class<T> clazz) {
        if (!ObjectUtils.isEmpty(value)) {
            List<T> result = new ArrayList<>();
            final String[] array = value.split(separator);
            for (final String item : array) {
                if (item == null) {
                    continue;
                } else if (ObjectUtils.isEmpty(item.trim())) {
                    continue;
                }
                result.add((T) ConvertUtils.convert(item, clazz));
            }
            return result;
        }
        return null;
    }

    public static List<String> split(final String value,
                                     final String separator) {
        return split(value, separator, String.class);
    }

    public static String formatTextarea(String value) {
        return value.replaceAll(PlatformConstant.ROW_SEPARATOR, "&#13;");
    }

    public static Map<String, Object> asMap(String metadata,
                                            String rowSeparator) {
        final Map<String, Object> result = new HashMap<>();

        if (ObjectUtils.isEmpty(metadata) || ObjectUtils.isEmpty(rowSeparator)) {
            return result;
        }

        final String[] all = metadata.split(rowSeparator);
        for (final String item : all) {
            if (ObjectUtils.isEmpty(item)) {
                continue;
            }
            int firstEqualsIndex = item.indexOf('=');
            if (firstEqualsIndex < 1) {
                continue;
            }
            final String key = item.substring(0, firstEqualsIndex);
            final String val = item.substring(firstEqualsIndex + 1);
            result.put(key, val);
        }
        return result;
    }
}