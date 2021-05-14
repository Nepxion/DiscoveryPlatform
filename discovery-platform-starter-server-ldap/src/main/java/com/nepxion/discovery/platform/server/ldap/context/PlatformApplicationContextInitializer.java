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

import com.nepxion.discovery.platform.server.ldap.service.LdapAdminService;
import com.nepxion.discovery.platform.server.ldap.service.LdapService;
import com.nepxion.discovery.platform.server.interfaces.AdminService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class PlatformApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof AdminService) {
                    return new LdapAdminService(
                            applicationContext.getBean(LdapService.class),
                            (AdminService) bean);
                }
                return super.postProcessAfterInitialization(bean, beanName);
            }
        });
    }
}