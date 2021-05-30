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
import com.nepxion.discovery.platform.server.controller.AdminPageController;
import com.nepxion.discovery.platform.server.controller.IndexController;
import com.nepxion.discovery.platform.server.controller.IndexPageController;
import com.nepxion.discovery.platform.server.controller.MenuController;
import com.nepxion.discovery.platform.server.controller.MenuPageController;
import com.nepxion.discovery.platform.server.controller.PermissionController;
import com.nepxion.discovery.platform.server.controller.PermissionPageController;
import com.nepxion.discovery.platform.server.controller.RoleController;
import com.nepxion.discovery.platform.server.controller.RolePageController;
import com.nepxion.discovery.platform.server.controller.RouteGatewayController;
import com.nepxion.discovery.platform.server.controller.RouteGatewayPageController;
import com.nepxion.discovery.platform.server.controller.RouteZuulController;
import com.nepxion.discovery.platform.server.controller.RouteZuulPageController;
import com.nepxion.discovery.platform.server.event.PlatformPublisher;
import com.nepxion.discovery.platform.server.event.PlatformSubscriber;
import com.nepxion.discovery.platform.server.properties.PlatformServerProperties;
import com.nepxion.discovery.platform.server.service.impl.MySqlAdminServiceImpl;
import com.nepxion.discovery.platform.server.service.impl.MySqlDicServiceImpl;
import com.nepxion.discovery.platform.server.service.impl.MySqlMenuServiceImpl;
import com.nepxion.discovery.platform.server.service.impl.MySqlPermissionServiceImpl;
import com.nepxion.discovery.platform.server.service.impl.MySqlRoleServiceImpl;
import com.nepxion.discovery.platform.server.service.impl.MySqlRouteGatewayServiceImpl;
import com.nepxion.discovery.platform.server.service.impl.MySqlRouteServiceImpl;
import com.nepxion.discovery.platform.server.service.impl.MySqlRouteZuulServiceImpl;
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
    public AdminPageController adminPageController() {
        return new AdminPageController();
    }

    @Bean
    public IndexPageController indexPageController() {
        return new IndexPageController();
    }

    @Bean
    public MenuPageController menuPageController() {
        return new MenuPageController();
    }

    @Bean
    public PermissionPageController permissionPageController() {
        return new PermissionPageController();
    }

    @Bean
    public RolePageController rolePageController() {
        return new RolePageController();
    }

    @Bean
    public RouteGatewayPageController routeGatewayPageController() {
        return new RouteGatewayPageController();
    }

    @Bean
    public RouteZuulPageController routeZuulPageController() {
        return new RouteZuulPageController();
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
    public MenuController menuController() {
        return new MenuController();
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
    public RouteZuulController routeZuulController() {
        return new RouteZuulController();
    }

    @Bean
    public MySqlAdminServiceImpl mysqlAdminService() {
        return new MySqlAdminServiceImpl();
    }

    @Bean
    public MySqlDicServiceImpl mysqlDicService() {
        return new MySqlDicServiceImpl();
    }

    @Bean
    public MySqlMenuServiceImpl mySqlMenuService() {
        return new MySqlMenuServiceImpl();
    }

    @Bean
    public MySqlPermissionServiceImpl mysqlPermissionService() {
        return new MySqlPermissionServiceImpl();
    }

    @Bean
    public MySqlRoleServiceImpl mysqlRoleService() {
        return new MySqlRoleServiceImpl();
    }

    @Bean
    public MySqlRouteServiceImpl mySqlRouteService() {
        return new MySqlRouteServiceImpl();
    }

    @Bean
    public MySqlRouteGatewayServiceImpl mysqlRouteGatewayService() {
        return new MySqlRouteGatewayServiceImpl();
    }

    @Bean
    public MySqlRouteZuulServiceImpl mySqlRouteZuulService() {
        return new MySqlRouteZuulServiceImpl();
    }

    @Bean
    public PlatformPublisher platformPublisher() {
        return new PlatformPublisher();
    }

    @Bean
    public PlatformSubscriber platformSubscriber() {
        return new PlatformSubscriber();
    }
}