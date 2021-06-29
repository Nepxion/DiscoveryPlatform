package com.nepxion.discovery.platform.server.shiro;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.service.MenuService;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtCredentialsMatcher extends SimpleCredentialsMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(JwtCredentialsMatcher.class);

    @Autowired
    private MenuService menuService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        AdminVo adminVo = (AdminVo) info.getPrincipals().getPrimaryPrincipal();
        try {
            menuService.fillPages(adminVo);
        } catch (Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return false;
        }
        return true;
    }
}