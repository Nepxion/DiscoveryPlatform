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

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.nepxion.discovery.console.configuration.ConsoleAutoConfiguration;
import com.nepxion.discovery.platform.server.adapter.PlatformDiscoveryAdapter;
import com.nepxion.discovery.platform.server.advice.ExceptionControllerAdvice;
import com.nepxion.discovery.platform.server.advice.ModelAdvice;
import com.nepxion.discovery.platform.server.controller.AdminController;
import com.nepxion.discovery.platform.server.controller.AdminPageController;
import com.nepxion.discovery.platform.server.controller.BlacklistController;
import com.nepxion.discovery.platform.server.controller.BlacklistPageController;
import com.nepxion.discovery.platform.server.controller.BlueGreenController;
import com.nepxion.discovery.platform.server.controller.BlueGreenPageController;
import com.nepxion.discovery.platform.server.controller.ConsoleController;
import com.nepxion.discovery.platform.server.controller.DashboardController;
import com.nepxion.discovery.platform.server.controller.DashboardPageController;
import com.nepxion.discovery.platform.server.controller.ErrorPageController;
import com.nepxion.discovery.platform.server.controller.GrayController;
import com.nepxion.discovery.platform.server.controller.GrayPageController;
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
import com.nepxion.discovery.platform.server.mapper.AdminMapper;
import com.nepxion.discovery.platform.server.properties.PlatformAuthProperties;
import com.nepxion.discovery.platform.server.properties.PlatformDataSourceProperties;
import com.nepxion.discovery.platform.server.properties.PlatformServerProperties;
import com.nepxion.discovery.platform.server.service.AdminServiceImpl;
import com.nepxion.discovery.platform.server.service.BlacklistService;
import com.nepxion.discovery.platform.server.service.BlacklistServiceImpl;
import com.nepxion.discovery.platform.server.service.BlueGreenService;
import com.nepxion.discovery.platform.server.service.BlueGreenServiceImpl;
import com.nepxion.discovery.platform.server.service.ConsoleService;
import com.nepxion.discovery.platform.server.service.ConsoleServiceImpl;
import com.nepxion.discovery.platform.server.service.DicServiceImpl;
import com.nepxion.discovery.platform.server.service.GrayService;
import com.nepxion.discovery.platform.server.service.GrayServiceImpl;
import com.nepxion.discovery.platform.server.service.MenuServiceImpl;
import com.nepxion.discovery.platform.server.service.PermissionServiceImpl;
import com.nepxion.discovery.platform.server.service.RoleServiceImpl;
import com.nepxion.discovery.platform.server.service.RouteGatewayServiceImpl;
import com.nepxion.discovery.platform.server.service.RouteServiceImpl;
import com.nepxion.discovery.platform.server.service.RouteZuulServiceImpl;
import com.nepxion.discovery.platform.server.shiro.JwtToolWrapper;
import com.nepxion.eventbus.annotation.EnableEventBus;

@Configuration
@AutoConfigureBefore(ConsoleAutoConfiguration.class)
@EnableEventBus
@EnableConfigurationProperties({ PlatformServerProperties.class, PlatformDataSourceProperties.class, PlatformAuthProperties.class })
@MapperScan(basePackageClasses = AdminMapper.class)
public class PlatformAutoConfiguration {
    @Bean
    public PlatformDiscoveryAdapter platformDiscoveryAdapter() {
        return new PlatformDiscoveryAdapter();
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
    public ExceptionControllerAdvice exceptionControllerAdvice() {
        return new ExceptionControllerAdvice();
    }

    @Bean
    public ModelAdvice modelAdvice() {
        return new ModelAdvice();
    }

    @Bean
    public JwtToolWrapper jwtToolWrapper(PlatformAuthProperties platformAuthProperties) {
        return new JwtToolWrapper(platformAuthProperties);
    }

    @Bean
    public DashboardPageController dashboardPageController() {
        return new DashboardPageController();
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
    public BlacklistPageController blacklistPageController() {
        return new BlacklistPageController();
    }

    @Bean
    public BlueGreenPageController blueGreenPageController() {
        return new BlueGreenPageController();
    }

    @Bean
    public GrayPageController grayPageController() {
        return new GrayPageController();
    }

    @Bean
    public DashboardController dashboardController() {
        return new DashboardController();
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
    public BlacklistController blacklistController() {
        return new BlacklistController();
    }

    @Bean
    public BlueGreenController blueGreenController() {
        return new BlueGreenController();
    }

    @Bean
    public GrayController grayController() {
        return new GrayController();
    }

    @Bean
    public ConsoleController consoleController() {
        return new ConsoleController();
    }

    @Bean
    public AdminServiceImpl adminService(JwtToolWrapper jwtToolWrapper) {
        AdminServiceImpl adminService = new AdminServiceImpl();
        adminService.setJwtToolWrapper(jwtToolWrapper);
        return adminService;
    }

    @Bean
    public DicServiceImpl dicService() {
        return new DicServiceImpl();
    }

    @Bean
    public MenuServiceImpl menuService() {
        return new MenuServiceImpl();
    }

    @Bean
    public PermissionServiceImpl permissionService() {
        return new PermissionServiceImpl();
    }

    @Bean
    public RoleServiceImpl roleService() {
        return new RoleServiceImpl();
    }

    @Bean
    public RouteServiceImpl routeService() {
        return new RouteServiceImpl();
    }

    @Bean
    public RouteGatewayServiceImpl routeGatewayService() {
        return new RouteGatewayServiceImpl();
    }

    @Bean
    public RouteZuulServiceImpl routeZuulService() {
        return new RouteZuulServiceImpl();
    }

    @Bean
    public BlacklistService blacklistService() {
        return new BlacklistServiceImpl();
    }

    @Bean
    public BlueGreenService blueGreenService() {
        return new BlueGreenServiceImpl();
    }

    @Bean
    public GrayService grayService() {
        return new GrayServiceImpl();
    }

    @Bean
    public ConsoleService consoleService() {
        return new ConsoleServiceImpl();
    }

    @Bean
    public ErrorPageController errorPageController() {
        return new ErrorPageController();
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                ErrorPage[] errorPages = new ErrorPage[1];
                errorPages[0] = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.do");
                registry.addErrorPages(errorPages);
            }
        };
    }
}