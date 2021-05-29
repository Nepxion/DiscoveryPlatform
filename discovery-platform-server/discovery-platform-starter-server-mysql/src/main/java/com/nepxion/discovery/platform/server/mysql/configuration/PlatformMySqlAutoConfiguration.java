package com.nepxion.discovery.platform.server.mysql.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.discovery.platform.server.adapter.PlatformLoginAdapter;
import com.nepxion.discovery.platform.server.mysql.adapter.PlatformMySqlLoginAdapter;
import com.nepxion.discovery.platform.server.mysql.constant.PlatformMySqlConstant;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlAdminMapper;
import com.nepxion.discovery.platform.server.mysql.properties.PlatformMySqlProperties;
import com.nepxion.discovery.platform.server.mysql.service.MySqlAdminService;
import com.nepxion.discovery.platform.server.mysql.service.MySqlDicService;
import com.nepxion.discovery.platform.server.mysql.service.MySqlPageService;
import com.nepxion.discovery.platform.server.mysql.service.MySqlPermissionService;
import com.nepxion.discovery.platform.server.mysql.service.MySqlRoleService;
import com.nepxion.discovery.platform.server.mysql.service.MySqlRouteGatewayService;
import com.nepxion.discovery.platform.server.mysql.service.MySqlRouteZuulService;
import com.nepxion.discovery.platform.server.mysql.tool.DataSourceTool;
import com.taobao.text.Color;

@Configuration
@MapperScan(basePackageClasses = MySqlAdminMapper.class)
@EnableConfigurationProperties({ PlatformMySqlProperties.class })
public class PlatformMySqlAutoConfiguration {
    static {
        LogoBanner logoBanner = new LogoBanner(PlatformMySqlAutoConfiguration.class, "/com/nepxion/mysql/resource/logo.txt", "Welcome to Nepxion", 5, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow }, true);
        NepxionBanner.show(logoBanner, new Description("Plugin:", PlatformMySqlConstant.MYSQL_TYPE, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Discovery", 0, 1));
    }

    @Autowired
    private PlatformMySqlProperties platformMySqlProperties;

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceTool.createHikariDataSource(
                platformMySqlProperties.getHost(),
                platformMySqlProperties.getPort(),
                platformMySqlProperties.getName(),
                platformMySqlProperties.getUsername(),
                platformMySqlProperties.getPassword(),
                platformMySqlProperties.getMinIdle(),
                platformMySqlProperties.getMaximum());
    }

    @Bean
    public PlatformLoginAdapter platformLoginAdapter() {
        return new PlatformMySqlLoginAdapter();
    }

    @Bean
    public MySqlAdminService mysqlAdminService() {
        return new MySqlAdminService();
    }

    @Bean
    public MySqlDicService mysqlDicService() {
        return new MySqlDicService();
    }

    @Bean
    public MySqlPageService mysqlPageService() {
        return new MySqlPageService();
    }

    @Bean
    public MySqlPermissionService mysqlPermissionService() {
        return new MySqlPermissionService();
    }

    @Bean
    public MySqlRoleService mysqlRoleService() {
        return new MySqlRoleService();
    }

    @Bean
    public MySqlRouteGatewayService mysqlRouteGatewayService() {
        return new MySqlRouteGatewayService();
    }

    @Bean
    public MySqlRouteZuulService mysqlRouteZuulService() {
        return new MySqlRouteZuulService();
    }
}