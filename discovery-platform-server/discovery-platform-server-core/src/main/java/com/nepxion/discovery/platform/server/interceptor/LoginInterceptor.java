package com.nepxion.discovery.platform.server.interceptor;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;

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
                final AdminVo adminVo = (AdminVo) SecurityUtils.getSubject().getPrincipal();
                if (null != adminVo) {
                    request.setAttribute(PlatformConstant.CURRENT_ADMIN_LOGIN, adminVo);
                }
            } catch (final Exception e) {
                LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            }
        }
        return true;
    }
}