package com.nepxion.discovery.platform.server.configuration.db;

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
import com.nepxion.discovery.platform.server.services.db.DbAdminService;
import com.nepxion.discovery.platform.server.services.db.DbPageService;
import com.nepxion.discovery.platform.server.services.db.DbPermissionService;
import com.nepxion.discovery.platform.server.services.db.DbRoleService;
import com.nepxion.discovery.platform.tool.db.CrudService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({PlatformProperties.class})
@ConditionalOnProperty(name = "platform.ui.mode", havingValue = "db")
public class PlatformDbAutoConfiguration {

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
    public AdminService adminService() {
        return new DbAdminService();
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