package com.nepxion.discovery.platform.mysql.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.tool.db.DataSourceTool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

import javax.sql.DataSource;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ComponentScan("com.nepxion.discovery.platform.mysql")
@MapperScan("com.nepxion.discovery.platform.mysql.mapper")
@EnableConfigurationProperties({PlatformMySqlProperties.class})
public class PlatformMySqlAutoConfiguration {

    private final PlatformMySqlProperties platformMySqlProperties;

    public PlatformMySqlAutoConfiguration(final PlatformMySqlProperties platformMySqlProperties) {
        this.platformMySqlProperties = platformMySqlProperties;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        return DataSourceTool.createHikariDataSource(
                this.platformMySqlProperties.getHost(),
                this.platformMySqlProperties.getPort(),
                this.platformMySqlProperties.getName(),
                this.platformMySqlProperties.getUsername(),
                this.platformMySqlProperties.getPassword(),
                this.platformMySqlProperties.getMinIdle(),
                this.platformMySqlProperties.getMaximum()
        );
    }
}