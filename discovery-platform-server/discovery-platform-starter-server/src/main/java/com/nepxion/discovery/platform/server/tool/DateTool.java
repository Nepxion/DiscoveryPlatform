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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;

import org.apache.commons.lang3.StringUtils;

public class DateTool {
    private static final DateFormat DATA_SEQUENCE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat TIME_SEQUENCE_FORMAT = new SimpleDateFormat("yyyyMMddhhmmssSSS");
    private static final DateFormat TIME_DAY = new SimpleDateFormat("yyyy-MM-dd");

    private static final List<DateFormat> DATE_FORMAT_LIST = Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd hh:mm"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy-MM"),

            new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"),
            new SimpleDateFormat("yyyy/MM/dd hh:mm"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("yyyy/MM"),

            new SimpleDateFormat("yyyy.MM.dd hh:mm:ss"),
            new SimpleDateFormat("yyyy.MM.dd hh:mm"),
            new SimpleDateFormat("yyyy.MM.dd"),
            new SimpleDateFormat("yyyy.MM")
    );

    public static Date parse(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        Date result = null;
        for (DateFormat dateFormat : DATE_FORMAT_LIST) {
            try {
                result = dateFormat.parse(value);
            } catch (ParseException ignored) {
            }
        }

        if (result == null) {
            throw new IllegalArgumentException(String.format("Invalid date value [%s]", value));
        }
        return result;
    }

    public static String getDataSequence() {
        return DATA_SEQUENCE_FORMAT.format(new Date());
    }

    public static String getTimeSequence() {
        return TIME_SEQUENCE_FORMAT.format(new Date());
    }

    public static String beginOfDay() {
        return TIME_DAY.format(new Date()) + " 00:00:00";
    }

    public static String getEndOfDay() {
        return TIME_DAY.format(new Date()) + " 23:59:59";
    }

}