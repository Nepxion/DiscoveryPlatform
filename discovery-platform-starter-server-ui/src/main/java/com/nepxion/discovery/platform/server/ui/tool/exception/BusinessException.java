package com.nepxion.discovery.platform.server.ui.tool.exception;

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

public final class BusinessException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public BusinessException(Throwable e) {
        super(ExceptionTool.getRootCauseMessage(e), e);
    }

    public BusinessException(final String errMsg) {
        super(errMsg);
    }
}