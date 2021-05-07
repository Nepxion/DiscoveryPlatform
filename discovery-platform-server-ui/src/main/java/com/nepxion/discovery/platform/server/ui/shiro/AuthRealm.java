package com.nepxion.discovery.platform.server.ui.shiro;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.ui.common.Tool;
import com.nepxion.discovery.platform.server.ui.entity.vo.Admin;
import com.nepxion.discovery.platform.server.ui.interceptor.LoginInterceptor;
import com.nepxion.discovery.platform.server.ui.interfaces.AdminService;
import com.nepxion.discovery.platform.server.ui.tool.exception.ExceptionTool;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthRealm extends AuthorizingRealm {
    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    private AdminService adminService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken authenticationToken) throws AuthenticationException {
        final UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        final String username = usernamePasswordToken.getUsername();
        final String password = new String(usernamePasswordToken.getPassword());
        Admin admin;
        try {
            if (!this.adminService.authenticate(username, Tool.hash(password))) {
                return null;
            }
            admin = this.adminService.getAdminByUserName(username);
        } catch (final Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return null;
        }
        return new SimpleAuthenticationInfo(admin, usernamePasswordToken.getPassword(), usernamePasswordToken.getUsername());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }
}