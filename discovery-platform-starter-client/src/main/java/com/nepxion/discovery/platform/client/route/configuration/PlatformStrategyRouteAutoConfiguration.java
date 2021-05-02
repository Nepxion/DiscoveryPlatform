package com.nepxion.discovery.platform.client.route.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.nepxion.discovery.platform.client.route.processor.PlatformGatewayStrategyRouteProcessor;
import com.nepxion.discovery.platform.client.route.processor.PlatformStrategyRouteProcessor;
import com.nepxion.discovery.plugin.strategy.gateway.route.GatewayStrategyRoute;
import com.nepxion.discovery.plugin.strategy.zuul.route.ZuulStrategyRoute;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class PlatformStrategyRouteAutoConfiguration {
    @ConditionalOnBean(GatewayStrategyRoute.class)
    protected static class GatewayStrategyRouteEndpointConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public PlatformGatewayStrategyRouteProcessor platformGatewayStrategyRouteProcessor() {
            return new PlatformGatewayStrategyRouteProcessor();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    protected static class ZuulStrategyRouteEndpointConfiguration {
        @Bean
        public PlatformStrategyRouteProcessor platformStrategyRouteProcessor() {
            return new PlatformStrategyRouteProcessor();
        }
    }
}