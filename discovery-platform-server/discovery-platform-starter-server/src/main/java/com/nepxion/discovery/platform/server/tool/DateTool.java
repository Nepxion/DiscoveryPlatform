package com.nepxion.discovery.platform.server.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DateTool {
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
}