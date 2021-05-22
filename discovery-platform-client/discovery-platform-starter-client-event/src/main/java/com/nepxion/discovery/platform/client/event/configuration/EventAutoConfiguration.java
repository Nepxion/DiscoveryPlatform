package com.nepxion.discovery.platform.client.event.configuration;

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

import com.nepxion.discovery.platform.client.event.subscriber.AlarmEventSubscriber;
import com.nepxion.discovery.platform.client.event.subscriber.GatewayRouteEventSubscriber;
import com.nepxion.discovery.platform.client.event.subscriber.RuleEventSubscriber;
import com.nepxion.discovery.platform.client.event.subscriber.ZuulRouteEventSubscriber;
import com.nepxion.discovery.plugin.strategy.constant.StrategyConstant;
import com.nepxion.discovery.plugin.strategy.gateway.route.GatewayStrategyRoute;
import com.nepxion.discovery.plugin.strategy.zuul.route.ZuulStrategyRoute;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class EventAutoConfiguration {
    protected static class EventConfiguration {
        @Bean
        public RuleEventSubscriber ruleEventSubscriber() {
            return new RuleEventSubscriber();
        }

        @Bean
        @ConditionalOnProperty(value = StrategyConstant.SPRING_APPLICATION_STRATEGY_ALARM_ENABLED, matchIfMissing = false)
        public AlarmEventSubscriber alarmEventSubscriber() {
            return new AlarmEventSubscriber();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    protected static class GatewayRouteEventConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public GatewayRouteEventSubscriber gatewayRouteEventSubscriber() {
            return new GatewayRouteEventSubscriber();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    protected static class ZuulRouteEventConfiguration {
        @Bean
        public ZuulRouteEventSubscriber zuulRouteEventSubscriber() {
            return new ZuulRouteEventSubscriber();
        }
    }
}