package com.nepxion.discovery.platform.server.configuration.memory;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.configuration.properties.PlatformProperties;
import com.nepxion.discovery.platform.server.ineterfaces.AdminService;
import com.nepxion.discovery.platform.server.ineterfaces.PageService;
import com.nepxion.discovery.platform.server.ineterfaces.PermissionService;
import com.nepxion.discovery.platform.server.ineterfaces.RoleService;
import com.nepxion.discovery.platform.server.services.memory.MemoryAdminService;
import com.nepxion.discovery.platform.server.services.memory.MemoryPageService;
import com.nepxion.discovery.platform.server.services.memory.MemoryPermissionService;
import com.nepxion.discovery.platform.server.services.memory.MemoryRoleService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties({PlatformProperties.class})
@ConditionalOnProperty(name = "platform.ui.mode", havingValue = "memory")
public class PlatformMemoryAutoConfiguration {

    private final PlatformProperties platformProperties;

    public PlatformMemoryAutoConfiguration(final PlatformProperties platformProperties) {
        this.platformProperties = platformProperties;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AdminService adminService() {
        return new MemoryAdminService();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public PageService pageService() {
        return new MemoryPageService();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public PermissionService permissionService() {
        return new MemoryPermissionService();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RoleService roleService() {
        return new MemoryRoleService();
    }

}