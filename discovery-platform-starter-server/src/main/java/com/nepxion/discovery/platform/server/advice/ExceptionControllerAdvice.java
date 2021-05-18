package com.nepxion.discovery.platform.server.advice;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.tool.exception.ExceptionTool;
import com.nepxion.discovery.platform.server.tool.web.Result;
import com.nepxion.discovery.platform.server.tool.web.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Object handleOtherException(final Model model,
                                       final HttpServletRequest request,
                                       final Exception e) {
        return handleException(model, request, e);
    }

    private Object handleException(final Model model,
                                   final HttpServletRequest request,
                                   final Exception exception) {
        final String errorMsg = ExceptionTool.getRootCauseMessage(exception);
        LOG.error(errorMsg, exception);
        if (isAjax(request)) {
            final ResultCode respondCode = ResultCode.get(errorMsg);
            if (null == respondCode) {
                return Result.error(errorMsg);
            } else {
                return Result.create(respondCode);
            }
        } else {
            model.addAttribute(PlatformConstant.ERROR, getStackTrace(exception));
            return new ModelAndView(PlatformConstant.ERROR_500);
        }
    }

    private boolean isAjax(final HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    private String getStackTrace(final Exception exception) {
        return ExceptionTool.getStackTraceInHtml(exception);
    }
}