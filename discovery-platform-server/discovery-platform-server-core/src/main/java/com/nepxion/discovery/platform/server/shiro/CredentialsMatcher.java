package com.nepxion.discovery.platform.server.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.service.PageService;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;

public class CredentialsMatcher extends SimpleCredentialsMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(CredentialsMatcher.class);
    @Autowired
    private PageService pageService;

    @Override
    public boolean doCredentialsMatch(final AuthenticationToken token,
                                      final AuthenticationInfo info) {
        final AdminVo adminVo = (AdminVo) info.getPrincipals().getPrimaryPrincipal();
        try {
            this.pageService.fillPages(adminVo);
        } catch (final Exception e) {
            LOG.error(ExceptionTool.getRootCauseMessage(e), e);
            return false;
        }
        return true;
    }
}