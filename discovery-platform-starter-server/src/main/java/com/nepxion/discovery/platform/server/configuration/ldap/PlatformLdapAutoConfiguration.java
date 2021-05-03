package com.nepxion.discovery.platform.server.configuration.ldap;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.nepxion.discovery.platform.server.configuration.properties.PlatformProperties;
import com.nepxion.discovery.platform.server.ineterfaces.AdminService;
import com.nepxion.discovery.platform.server.ineterfaces.PageService;
import com.nepxion.discovery.platform.server.ineterfaces.PermissionService;
import com.nepxion.discovery.platform.server.ineterfaces.RoleService;
import com.nepxion.discovery.platform.server.services.LdapService;
import com.nepxion.discovery.platform.server.services.db.DbPageService;
import com.nepxion.discovery.platform.server.services.db.DbPermissionService;
import com.nepxion.discovery.platform.server.services.db.DbRoleService;
import com.nepxion.discovery.platform.server.services.ldap.LdapAdminService;
import com.nepxion.discovery.platform.tool.db.CrudService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({PlatformProperties.class})
@ConditionalOnProperty(name = "platform.ui.mode", havingValue = "ldap")
public class PlatformLdapAutoConfiguration {

    private final PlatformProperties platformProperties;
    private final LdapProperties ldapProperties;
    private final Environment environment;

    public PlatformLdapAutoConfiguration(final PlatformProperties platformProperties,
                                         final LdapProperties ldapProperties,
                                         final Environment environment) {
        this.platformProperties = platformProperties;
        this.ldapProperties = ldapProperties;
        this.environment = environment;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        final PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public CrudService crudService(final DataSource dataSource) {
        return new CrudService(dataSource);
    }


    @Bean
    @Primary
    @ConditionalOnMissingBean
    public LdapContextSource contextSource() {
        final LdapContextSource contextSource = new LdapContextSource();
        final Map<String, Object> config = new HashMap();
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
                platformProperties.getLdap());
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AdminService adminService() {
        return new LdapAdminService();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public PageService pageService() {
        return new DbPageService();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public PermissionService permissionService() {
        return new DbPermissionService();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RoleService roleService() {
        return new DbRoleService();
    }
}