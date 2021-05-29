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

import java.io.PrintStream;

import org.springframework.util.FastByteArrayOutputStream;

public class ExceptionTool {
    public static String getRootCauseMessage(Throwable throwable) {
        if (throwable.getCause() != null) {
            return getRootCauseMessage(throwable.getCause());
        }
        return throwable.getMessage() == null ? throwable.toString() : throwable.getMessage();
    }

    public static String getStackTrace(Throwable throwable) {
        FastByteArrayOutputStream result = new FastByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(result));
        return result.toString().trim();
    }

    public static String getStackTraceInHtml(Throwable throwable) {
        return getStackTrace(throwable).replaceAll("\\r\\n\\t", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    }
}