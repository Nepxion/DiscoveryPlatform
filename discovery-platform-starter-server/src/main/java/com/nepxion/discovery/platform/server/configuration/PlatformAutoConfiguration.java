package com.nepxion.discovery.platform.server.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.discovery.platform.server.advice.ExceptionControllerAdvice;
import com.nepxion.discovery.platform.server.advice.ModelAdvice;
import com.nepxion.discovery.platform.server.controller.AdminController;
import com.nepxion.discovery.platform.server.controller.IndexController;
import com.nepxion.discovery.platform.server.controller.PageController;
import com.nepxion.discovery.platform.server.controller.PermissionController;
import com.nepxion.discovery.platform.server.controller.RoleController;
import com.nepxion.discovery.platform.server.controller.RouteGatewayController;
import com.nepxion.discovery.platform.server.event.PlatformEventWapper;
import com.nepxion.discovery.platform.server.event.PlatformPublisher;
import com.nepxion.discovery.platform.server.event.PlatformSubscriber;
import com.nepxion.discovery.platform.server.properties.PlatformServerProperties;
import com.nepxion.eventbus.annotation.EnableEventBus;

@Configuration
@EnableEventBus
@EnableConfigurationProperties({ PlatformServerProperties.class })
public class PlatformAutoConfiguration {
    @Bean
    public ExceptionControllerAdvice exceptionControllerAdvice() {
        return new ExceptionControllerAdvice();
    }

    @Bean
    public ModelAdvice modelAdvice() {
        return new ModelAdvice();
    }

    @Bean
    public AdminController adminController() {
        return new AdminController();
    }

    @Bean
    public IndexController indexController() {
        return new IndexController();
    }

    @Bean
    public PageController pageController() {
        return new PageController();
    }

    @Bean
    public PermissionController permissionController() {
        return new PermissionController();
    }

    @Bean
    public RoleController roleController() {
        return new RoleController();
    }

    @Bean
    public RouteGatewayController routeGatewayController() {
        return new RouteGatewayController();
    }

    @Bean
    public PlatformPublisher platformPublisher() {
        return new PlatformPublisher();
    }

    @Bean
    public PlatformSubscriber platformSubscriber() {
        return new PlatformSubscriber();
    }

    @Bean
    public PlatformEventWapper platformEventWapper() {
        return new PlatformEventWapper();
    }
}