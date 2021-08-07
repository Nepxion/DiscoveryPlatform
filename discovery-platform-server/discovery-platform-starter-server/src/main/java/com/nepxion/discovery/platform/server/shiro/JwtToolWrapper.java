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

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.UnsatisfiedDependencyException;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.properties.PlatformAuthProperties;
import com.nepxion.discovery.platform.server.tool.JwtTool;

public class JwtToolWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(JwtToolWrapper.class);

    private static final String DEFAULT_SECRET = "LvQg8gdN2HwDx9SWO3j9bbBtgiGJLG8A";
    private static final Integer DEFAULT_RENEW_THRESHOLD = 1;

    private PlatformAuthProperties authProperties;
    private PlatformAuthProperties.TokenProperties tokenProperties;

    public JwtToolWrapper(PlatformAuthProperties authProperties) {
        this.authProperties = authProperties;
        this.tokenProperties = authProperties.getToken();
        propertiesPostCheck();
    }

    public String generateBearerToken(AdminVo adminVo) {
        return DiscoveryConstant.BEARER + " " + generateToken(adminVo);
    }

    public String generateToken(AdminVo adminVo) {
        return JwtTool.generateToken(adminVo, tokenProperties.getSecret(),
                tokenProperties.getExpireTime(), tokenProperties.getMaxLiveTime());
    }

    public boolean verify(String token) {
        return JwtTool.verify(token, tokenProperties.getSecret());
    }

    public long decodeToken(String token) {
        return JwtTool.decodeToken(token);
    }

    public String refreshBearerTokenIfNecessary(String token) {
        String newToken = JwtTool.refreshTokenIfNecessary(token,
                tokenProperties.getSecret(), tokenProperties.getExpireTime(),
                tokenProperties.getRenewThreshold());
        return null == newToken ? null : DiscoveryConstant.BEARER + " " + newToken;
    }

    public String refreshTokenIfNecessary(String token) {
        return JwtTool.refreshTokenIfNecessary(token, tokenProperties.getSecret(),
                tokenProperties.getExpireTime(), tokenProperties.getRenewThreshold());
    }

    private void propertiesPostCheck() {
        if (null == authProperties || null == tokenProperties) {
            throw new UnsatisfiedDependencyException("Unsatisfied dependency for bean initialization",
                    JwtToolWrapper.class.getName(), "[authProperties, tokenProperties]", "");
        }

        String secret = StringUtils.isBlank(tokenProperties.getSecret())
                ? DEFAULT_SECRET : tokenProperties.getSecret();
        tokenProperties.setSecret(secret);

        Integer renewThreshold = tokenProperties.getRenewThreshold();
        if (renewThreshold < DEFAULT_RENEW_THRESHOLD || renewThreshold > 10) {
            LOG.warn("JWT renew threshold is less than 1 or bigger than 10, use default value: 1");
            renewThreshold = DEFAULT_RENEW_THRESHOLD;
        }
        tokenProperties.setRenewThreshold(renewThreshold);
    }
}