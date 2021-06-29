package com.nepxion.discovery.platform.server.filter;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;
import com.nepxion.discovery.platform.server.tool.JwtTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */
public class ShiroJwtFilter extends BasicHttpAuthenticationFilter {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public static final String BEARER_PREFIX = "Bearer";

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return ((HttpServletRequest) request).getHeader(DiscoveryConstant.ACCESS_TOKEN) == null;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String tokenString = httpServletRequest.getHeader(DiscoveryConstant.ACCESS_TOKEN);

        try {
            tokenString = parseTokenValue(tokenString);
            return new BearerToken(tokenString);
        } catch (IllegalArgumentException e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
        }
        return null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        httpResponse.setContentType(MediaType.APPLICATION_JSON.toString());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        httpResponse.setStatus(status.value());
        Result<Object> result = Result.error(status.value(), status.getReasonPhrase());
        httpResponse.getWriter().write(JsonUtil.toJson(result));
        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if (token instanceof BearerToken) {
            newToken = JwtTool.refreshTokenIfNecessary(((BearerToken) token).getToken());
        }
        if (newToken != null) {
            httpResponse.setHeader(DiscoveryConstant.ACCESS_TOKEN, newToken);
        }
        return true;
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                httpServletRequest.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS));
    }

    protected String parseTokenValue(String token) {
        if (isBearerToken(token)) {
            return token.substring(BEARER_PREFIX.length()).trim();
        }
        throw new IllegalArgumentException("Unknown token type begin with " + token.substring(10));
    }

    private boolean isBearerToken(String token) {
        return StringUtils.startsWith(token, BEARER_PREFIX);
    }

}
