package com.nepxion.discovery.platform.server.ui.configuration.properties;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.ui.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.ui.service.LdapService;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@ConfigurationProperties("platform.server")
public class PlatformServerProperties implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    /**
     * 标题
     */
    private String title = "";

    /**
     * 应用全名称
     */
    private String fullName = "";

    /**
     * 应用简称
     */
    private String shortName = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public LoginMode getLoginMode() {
        String[] beanNamesForType = this.applicationContext.getBeanNamesForType(LdapService.class);
        if (beanNamesForType.length > 0) {
            return LoginMode.LDAP;
        }
        return LoginMode.DB;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
