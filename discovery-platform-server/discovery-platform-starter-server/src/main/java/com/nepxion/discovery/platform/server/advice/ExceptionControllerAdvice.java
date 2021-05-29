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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.response.ResultCode;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Object handleOtherException(Model model, HttpServletRequest request, Exception e) {
        return handleException(model, request, e);
    }

    private Object handleException(Model model, HttpServletRequest request, Exception exception) {
        String errorMsg = ExceptionTool.getRootCauseMessage(exception);
        LOG.error(errorMsg, exception);
        if (isAjax(request)) {
            ResultCode respondCode = ResultCode.get(errorMsg);
            if (respondCode == null) {
                return Result.error(errorMsg);
            } else {
                return Result.create(respondCode);
            }
        } else {
            model.addAttribute(PlatformConstant.ERROR, getStackTrace(exception));
            return new ModelAndView(PlatformConstant.ERROR_500);
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    private String getStackTrace(Exception exception) {
        return ExceptionTool.getStackTraceInHtml(exception);
    }
}