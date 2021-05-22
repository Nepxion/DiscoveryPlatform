package com.nepxion.discovery.platform.server.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;

import javax.servlet.http.HttpServletRequest;

public class LoginArgumentResolver implements WebArgumentResolver {
    @Override
    public Object resolveArgument(final MethodParameter methodParameter,
                                  final NativeWebRequest nativeWebRequest) {
        final Class<?> parameterType = methodParameter.getParameterType();
        final HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (null != request && parameterType.equals(AdminVo.class)) {
            return request.getAttribute(PlatformConstant.CURRENT_ADMIN_LOGIN);
        }
        return UNRESOLVED;
    }
}