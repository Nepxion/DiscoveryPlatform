package com.nepxion.discovery.platform.server.resolver;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

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