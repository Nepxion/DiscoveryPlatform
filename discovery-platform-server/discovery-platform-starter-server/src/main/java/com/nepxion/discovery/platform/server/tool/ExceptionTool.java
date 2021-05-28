package com.nepxion.discovery.platform.server.tool;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.exceptions.ExceptionUtil;

public class ExceptionTool {
    public static String getRootCauseMessage(Throwable throwable) {
        Throwable rootCause = ExceptionUtil.getRootCause(throwable);
        if (rootCause == null) {
            return StringUtils.EMPTY;
        }
        return rootCause.getMessage() == null ? rootCause.toString() : rootCause.getMessage();
    }

    public static String getMessage(Throwable throwable) {
        return ExceptionUtil.getMessage(throwable);
    }

    public static String getStackTrace(Throwable throwable) {
        return ExceptionUtil.stacktraceToString(throwable);
    }

    public static String getStackTraceInHtml(Throwable throwable) {
        return getStackTrace(throwable).replaceAll("\\r\\n\\t", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    }
}