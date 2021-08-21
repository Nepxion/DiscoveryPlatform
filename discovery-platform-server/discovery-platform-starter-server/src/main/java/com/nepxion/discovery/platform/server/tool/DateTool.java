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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class DateTool {
    private static final String DATA_SEQUENCE_PATTERN = "yyyyMMdd";
    private static final String TIME_SEQUENCE_PATTERN = "yyyyMMddhhmmssSSS";
    private static final String TIME_DAY_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<List<DateFormat>> THREAD_LOCAL_DATE_FORMAT_LIST =
            ThreadLocal.withInitial(() -> Arrays.asList(
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
            ));

    public static Date parse(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        Date result = null;
        for (DateFormat dateFormat : THREAD_LOCAL_DATE_FORMAT_LIST.get()) {
            try {
                result = dateFormat.parse(value);
                if (Objects.nonNull(result)) {
                    break;
                }
            } catch (ParseException ignored) {
            }
        }

        if (result == null) {
            throw new IllegalArgumentException(String.format("Invalid date value [%s]", value));
        }
        return result;
    }

    public static String getDataSequence() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATA_SEQUENCE_PATTERN));
    }

    public static String getTimeSequence() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_SEQUENCE_PATTERN));
    }

    public static String beginOfDay() {
        return LocalDate.now().atTime(LocalTime.MIN)
                .format(DateTimeFormatter.ofPattern(TIME_DAY_PATTERN));
    }

    public static String getEndOfDay() {
        return LocalDate.now().plusDays(1L).atTime(LocalTime.MIN)
                .format(DateTimeFormatter.ofPattern(TIME_DAY_PATTERN));
    }
}