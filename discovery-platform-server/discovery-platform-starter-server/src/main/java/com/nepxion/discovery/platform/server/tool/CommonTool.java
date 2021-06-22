package com.nepxion.discovery.platform.server.tool;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.nepxion.discovery.platform.server.constant.PlatformConstant;

public class CommonTool {
    private static final Logger LOG = LoggerFactory.getLogger(CommonTool.class);
    private static final String SALT = "PEgASuS";

    public static <T> void addKVForList(Map<String, List<T>> map, String key, T value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<T> valueList = new ArrayList<>();
            valueList.add(value);
            map.put(key, valueList);
        }
    }

    public static <T> void addKVForSet(Map<String, Set<String>> map, String key, String value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            Set<String> valueList = new LinkedHashSet<>();
            valueList.add(value);
            map.put(key, valueList);
        }
    }

    public static void sleep(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
        }
    }

    public static String getVersion() {
        return System.getProperty("version", PlatformConstant.PLATFORM_VERSION);
    }

    public static String hash(String value) {
        return new Md5Hash(value, SALT).toString();
    }

    public static <T> T toVo(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        try {
            T result = target.newInstance();
            BeanUtils.copyProperties(source, result);
            return result;
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return null;
        }
    }

    public static <T> List<T> toVoList(List<?> source, Class<T> target) {
        if (source == null) {
            return null;
        }
        try {
            List<T> result = new ArrayList<>(source.size());
            for (Object o : source) {
                result.add(toVo(o, target));
            }
            return result;
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> parseList(String value, String separate, Class<T> tClass) {
        String[] array = value.split(separate);
        List<T> result = new ArrayList<>(array.length);
        for (String item : array) {
            if (StringUtils.isEmpty(item)) {
                continue;
            }
            result.add((T) ConvertUtils.convert(item, tClass));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> split(String value, String separator, Class<T> clazz) {
        if (StringUtils.isNotEmpty(value)) {
            List<T> result = new ArrayList<>();
            String[] array = value.split(separator);
            for (String item : array) {
                if (item == null) {
                    continue;
                } else if (StringUtils.isEmpty(item.trim())) {
                    continue;
                }
                result.add((T) ConvertUtils.convert(item, clazz));
            }
            return result;
        }
        return new ArrayList<>();
    }

    public static List<String> split(String value, String separator) {
        return split(value, separator, String.class);
    }

    public static String formatTextarea(String value) {
        return value.replaceAll(PlatformConstant.ROW_SEPARATOR, "&#13;");
    }

    public static Map<String, Object> asMap(String metadata, String rowSeparator) {
        Map<String, Object> result = new HashMap<>();

        if (StringUtils.isEmpty(metadata) || StringUtils.isEmpty(rowSeparator)) {
            return result;
        }

        String[] all = metadata.split(rowSeparator);
        for (String item : all) {
            if (StringUtils.isEmpty(item)) {
                continue;
            }
            int firstEqualsIndex = item.indexOf('=');
            if (firstEqualsIndex < 1) {
                continue;
            }
            String key = item.substring(0, firstEqualsIndex);
            String val = item.substring(firstEqualsIndex + 1);
            result.put(key, val);
        }
        return result;
    }
}