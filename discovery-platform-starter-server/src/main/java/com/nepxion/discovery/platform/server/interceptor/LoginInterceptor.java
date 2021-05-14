package com.nepxion.discovery.platform.server.interceptor;

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
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.tool.exception.ExceptionTool;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements AsyncHandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            try {
                final Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
                if (null != admin) {
                    request.setAttribute(PlatformConstant.CURRENT_ADMIN_LOGIN, admin);
                }
            } catch (final Exception e) {
                LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            }
        }
        return true;
    }
}