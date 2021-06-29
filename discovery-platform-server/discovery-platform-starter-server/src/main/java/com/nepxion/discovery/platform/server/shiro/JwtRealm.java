package com.nepxion.discovery.platform.server.shiro;

import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.interceptor.LoginInterceptor;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.MenuService;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;
import com.nepxion.discovery.platform.server.tool.JwtTool;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */
public class JwtRealm extends AuthorizingRealm {
    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private MenuService menuService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return BearerToken.class.isAssignableFrom(token.getClass());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        BearerToken token = (BearerToken) authenticationToken;
        String tokenString = token.getToken();

        try {
            if (JwtTool.verify(tokenString)) {
                long id = JwtTool.decodeToken(tokenString);
                AdminVo adminVo = adminService.getAdminById(id);
                if (adminService.isSuperAdmin(adminVo.getUsername())) {
                    adminVo.getSysRole().setSuperAdmin(true);
                } else {
                    adminVo.getSysRole().setSuperAdmin(false);
                }
                return new SimpleAuthenticationInfo(adminVo, null, adminVo.getUsername());
            }
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return null;
        }
        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        AdminVo adminVo = (AdminVo) principalCollection.getPrimaryPrincipal();
        try {
            menuService.fillPages(adminVo);
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return null;
        }
        return new SimpleAuthorizationInfo();
    }
}