package com.nepxion.discovery.platform.server.exception;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.io.Serializable;

import com.nepxion.discovery.platform.server.tool.ExceptionTool;

public class PlatformException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -1046678240722236088L;

    public PlatformException(Throwable e) {
        super(ExceptionTool.getRootCauseMessage(e), e);
    }

    public PlatformException(String errMsg) {
        super(errMsg);
    }
}