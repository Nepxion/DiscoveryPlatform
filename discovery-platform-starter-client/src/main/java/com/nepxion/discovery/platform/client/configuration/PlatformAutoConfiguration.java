package com.nepxion.discovery.platform.client.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.nepxion.discovery.platform.client.event.PlatformAlarmEventSubscriber;
import com.nepxion.discovery.platform.client.event.PlatformGatewayRouteEventSubscriber;
import com.nepxion.discovery.platform.client.event.PlatformRuleEventSubscriber;
import com.nepxion.discovery.platform.client.event.PlatformZuulRouteEventSubscriber;
import com.nepxion.discovery.plugin.strategy.gateway.route.GatewayStrategyRoute;
import com.nepxion.discovery.plugin.strategy.zuul.route.ZuulStrategyRoute;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class PlatformAutoConfiguration {
    protected static class PlatformConfiguration {
        @Bean
        public PlatformRuleEventSubscriber platformRuleEventSubscriber() {
            return new PlatformRuleEventSubscriber();
        }

        @Bean
        public PlatformAlarmEventSubscriber platformAlarmEventSubscriber() {
            return new PlatformAlarmEventSubscriber();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    protected static class PlatformGatewayRouteEventConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public PlatformGatewayRouteEventSubscriber platformGatewayRouteEventSubscriber() {
            return new PlatformGatewayRouteEventSubscriber();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    protected static class PlatformZuulRouteEventConfiguration {
        @Bean
        public PlatformZuulRouteEventSubscriber platformZuulRouteEventSubscriber() {
            return new PlatformZuulRouteEventSubscriber();
        }
    }
}