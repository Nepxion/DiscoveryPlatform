package com.nepxion.discovery.platform.tool.common;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.alibaba.fastjson.JSONValidator;
import com.nepxion.discovery.platform.tool.db.DataSourceTool;
import com.nepxion.discovery.platform.tool.exception.ExceptionTool;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

public final class CommonTool {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceTool.class);

    private final static long KB_IN_BYTES = 1024;
    private final static long MB_IN_BYTES = 1024 * KB_IN_BYTES;
    private final static long GB_IN_BYTES = 1024 * MB_IN_BYTES;
    private final static long TB_IN_BYTES = 1024 * GB_IN_BYTES;
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

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

    public static boolean isJson(final String value) {
        if (ObjectUtils.isEmpty(value)) {
            return false;
        }

        try (final JSONValidator from = JSONValidator.from(value)) {
            return from.validate();
        } catch (IOException e) {
            return false;
        }
    }

    public static Date getMinDate() {
        return new Date(0);
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

    public static void sleep(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
        }
    }

    public static double toDouble(final String number) {
        return toDouble(Double.valueOf(number));
    }

    public static double toDouble(final Double number) {
        return Double.parseDouble(DECIMAL_FORMAT.format(number));
    }

    public static String convertSize(long byteNumber) {
        if (byteNumber / TB_IN_BYTES > 0) {
            return String.format("%sTB", DECIMAL_FORMAT.format((double) byteNumber / (double) TB_IN_BYTES));
        } else if (byteNumber / GB_IN_BYTES > 0) {
            return String.format("%sGB", DECIMAL_FORMAT.format((double) byteNumber / (double) GB_IN_BYTES));
        } else if (byteNumber / MB_IN_BYTES > 0) {
            return String.format("%sMB", DECIMAL_FORMAT.format((double) byteNumber / (double) MB_IN_BYTES));
        } else if (byteNumber / KB_IN_BYTES > 0) {
            return String.format("%sKB", DECIMAL_FORMAT.format((double) byteNumber / (double) KB_IN_BYTES));
        } else {
            return String.format("%sB", byteNumber);
        }
    }

    public static String convertSize(String number) {
        return convertSize(Math.round(toDouble(number)));
    }

    public static Map<String, Object> queryParamsToMap(final String queryParams) {
        final Map<String, Object> result = new HashMap<>();
        if (ObjectUtils.isEmpty(queryParams)) {
            return result;
        }
        final String query;
        try {
            query = URLDecoder.decode(queryParams.trim(), StandardCharsets.UTF_8.name());
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        final String[] pairs = query.split("&");
        for (final String pair : pairs) {
            final String[] kv = pair.split("=");
            if (2 != kv.length) {
                continue;
            }
            result.put(kv[0], kv[1]);
        }
        return result;
    }

    public static Map<String, Object> queryParamsToMap(final byte[] queryParamsByte) {
        String queryParams = "";
        if (null != queryParamsByte) {
            queryParams = new String(queryParamsByte);
        }
        return queryParamsToMap(queryParams);
    }

    public static Map<String, Object> queryParamsToMap(final Object queryParamsObject) {
        if (null == queryParamsObject) {
            return new HashMap<>();
        } else if (queryParamsObject instanceof byte[]) {
            return queryParamsToMap((byte[]) queryParamsObject);
        } else if (queryParamsObject instanceof String) {
            return queryParamsToMap((String) queryParamsObject);
        }
        throw new RuntimeException(String.format("%s type not supported", queryParamsObject.getClass()));
    }
}