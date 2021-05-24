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

import com.nepxion.discovery.platform.client.event.AlarmEventSubscriber;
import com.nepxion.discovery.platform.client.event.GatewayRouteEventSubscriber;
import com.nepxion.discovery.platform.client.event.RuleEventSubscriber;
import com.nepxion.discovery.platform.client.event.ZuulRouteEventSubscriber;
import com.nepxion.discovery.plugin.strategy.gateway.route.GatewayStrategyRoute;
import com.nepxion.discovery.plugin.strategy.zuul.route.ZuulStrategyRoute;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class PlatformAutoConfiguration {
    protected static class PlatformConfiguration {
        @Bean
        public RuleEventSubscriber ruleEventSubscriber() {
            return new RuleEventSubscriber();
        }

        @Bean
        public AlarmEventSubscriber alarmEventSubscriber() {
            return new AlarmEventSubscriber();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    protected static class PlatformGatewayRouteEventConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public GatewayRouteEventSubscriber gatewayRouteEventSubscriber() {
            return new GatewayRouteEventSubscriber();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    protected static class PlatformZuulRouteEventConfiguration {
        @Bean
        public ZuulRouteEventSubscriber zuulRouteEventSubscriber() {
            return new ZuulRouteEventSubscriber();
        }
    }
}