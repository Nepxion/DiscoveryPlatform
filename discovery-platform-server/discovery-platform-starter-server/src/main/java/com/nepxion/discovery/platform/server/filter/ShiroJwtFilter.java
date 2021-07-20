package com.nepxion.discovery.platform.server.filter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.shiro.JwtToolWrapper;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;

public class ShiroJwtFilter extends BasicHttpAuthenticationFilter {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public static final String N_D_SESSION_STATUS = "n-d-session-status";

    public static final String SESSION_STATUS_EXPIRED = "expired";

    public static final String SESSION_STATUS_INVALID = "invalid";

    private JwtToolWrapper jwtToolWrapper;

    public ShiroJwtFilter(JwtToolWrapper jwtToolWrapper) {
        this.jwtToolWrapper = jwtToolWrapper;
    }

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
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        fillCorsHeader(WebUtils.toHttp(request), httpServletResponse);

        String sessionStatus = httpServletResponse.getHeader(N_D_SESSION_STATUS);
        if (StringUtils.isNotBlank(sessionStatus)) {
            httpServletResponse.resetBuffer();

            buildAuthFailureResponse(httpServletResponse);
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        Throwable throwable = null;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
        } catch (JWTVerificationException e) {
            throwable = e;
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
        }
        if (!allowed && isAjaxRequest(WebUtils.toHttp(request))) {
            addFailureStatus(response, throwable);
            return Boolean.TRUE;
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return ((HttpServletRequest) request).getHeader(DiscoveryConstant.N_D_ACCESS_TOKEN) == null;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String tokenString = httpServletRequest.getHeader(DiscoveryConstant.N_D_ACCESS_TOKEN);

        try {
            tokenString = parseTokenValue(tokenString);
            jwtToolWrapper.verify(tokenString);
            return new BearerToken(tokenString);
        } catch (JWTVerificationException e) {
            throw e;
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
            newToken = jwtToolWrapper.refreshBearerTokenIfNecessary(((BearerToken) token).getToken());
        }
        if (newToken != null) {
            httpResponse.setHeader(DiscoveryConstant.N_D_ACCESS_TOKEN, newToken);
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
            return token.substring(DiscoveryConstant.BEARER.length()).trim();
        }
        throw new IllegalArgumentException("Unknown token type begin with: "
                + StringUtils.substring(token, 0, 10));
    }

    private boolean isBearerToken(String token) {
        return StringUtils.startsWith(token, DiscoveryConstant.BEARER);
    }

    public boolean isAjaxRequest(HttpServletRequest request) {
        return StringUtils.equalsIgnoreCase("XMLHttpRequest", request.getHeader("X-Requested-With"));
    }

    public void addFailureStatus(ServletResponse servletResponse, Throwable throwable) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        String status = SESSION_STATUS_INVALID;
        if (throwable instanceof TokenExpiredException) {
            status = SESSION_STATUS_EXPIRED;
        }
        response.setHeader(N_D_SESSION_STATUS, status);
    }

    public void buildAuthFailureResponse(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        response.setStatus(HttpStatus.OK.value());
        Result<String> result = Result.ok(status.value(), status.getReasonPhrase());
        response.getWriter().write(JsonUtil.toJson(result));
    }

}
