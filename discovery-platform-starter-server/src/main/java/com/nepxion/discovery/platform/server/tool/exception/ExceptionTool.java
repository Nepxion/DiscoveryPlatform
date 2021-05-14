package com.nepxion.discovery.platform.server.tool.exception;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import cn.hutool.core.exceptions.ExceptionUtil;

public final class ExceptionTool {
    public static String getRootCauseMessage(final Throwable throwable) {
        Throwable rootCause = ExceptionUtil.getRootCause(throwable);
        if (null == rootCause) {
            return "";
        }
        return null == rootCause.getMessage() ? rootCause.toString() : rootCause.getMessage();
    }

    public static String getMessage(final Throwable throwable) {
        return ExceptionUtil.getMessage(throwable);
    }

    public static String getStackTrace(final Throwable throwable) {
        return ExceptionUtil.stacktraceToString(throwable);
    }

    public static String getStackTraceInHtml(final Throwable throwable) {
        return getStackTrace(throwable).replaceAll("\\r\\n\\t", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    }
}