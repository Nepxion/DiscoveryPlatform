package com.nepxion.discovery.platform.server.ldap.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.ldap.configuration.properties.PlatformLdapProperties;
import com.nepxion.discovery.platform.server.ldap.service.LdapService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({PlatformLdapProperties.class})
public class PlatformLdapAutoConfiguration {
    private final LdapProperties ldapProperties;
    private final Environment environment;
    private final PlatformLdapProperties platformLdapProperties;

    public PlatformLdapAutoConfiguration(final LdapProperties ldapProperties,
                                         final Environment environment,
                                         final PlatformLdapProperties platformLdapProperties) {
        this.ldapProperties = ldapProperties;
        this.environment = environment;
        this.platformLdapProperties = platformLdapProperties;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public LdapContextSource contextSource() {
        final LdapContextSource contextSource = new LdapContextSource();
        final Map<String, Object> config = new HashMap<>();
        config.put("java.naming.ldap.attributes.binary", "objectGUID");
        contextSource.setCacheEnvironmentProperties(false);
        contextSource.setUrls(this.ldapProperties.determineUrls(this.environment));
        contextSource.setUserDn(this.ldapProperties.getUsername());
        contextSource.setPassword(this.ldapProperties.getPassword());
        contextSource.setPooled(true);
        contextSource.setBaseEnvironmentProperties(config);
        return contextSource;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public LdapTemplate ldapTemplate(final ContextSource contextSource) {
        final LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.setIgnorePartialResultException(true);
        return ldapTemplate;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public LdapService ldapService(final LdapTemplate ldapTemplate,
                                   final LdapProperties ldapProperties) {
        return new LdapService(ldapTemplate,
                ldapProperties,
                this.platformLdapProperties);
    }
}