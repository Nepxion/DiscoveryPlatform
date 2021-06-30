package com.nepxion.discovery.platform.server.ldap.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.shiro.JwtToolWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.nepxion.discovery.platform.server.ldap.service.LdapAdminService;
import com.nepxion.discovery.platform.server.ldap.service.LdapService;
import com.nepxion.discovery.platform.server.service.AdminService;

public class PlatformLdapApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof AdminService) {
                    LdapService ldapService = applicationContext.getBean(LdapService.class);
                    JwtToolWrapper jwtToolWrapper = applicationContext.getBean(JwtToolWrapper.class);
                    AdminService adminService = (AdminService) bean;

                    LdapAdminService ldapAdminService = new LdapAdminService(ldapService, adminService);
                    ldapAdminService.setJwtToolWrapper(jwtToolWrapper);
                    return ldapAdminService;
                }

                return super.postProcessAfterInitialization(bean, beanName);
            }
        });
    }
}