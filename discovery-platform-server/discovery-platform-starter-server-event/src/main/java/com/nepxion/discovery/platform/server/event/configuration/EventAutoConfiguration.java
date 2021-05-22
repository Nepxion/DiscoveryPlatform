package com.nepxion.discovery.platform.server.event.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.nepxion.discovery.platform.server.event.publisher.EventPublisher;
import com.nepxion.discovery.platform.server.event.subscriber.RouteAlarmEventSubscriber;
import com.nepxion.eventbus.annotation.EnableEventBus;

@Configuration
@EnableEventBus
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class EventAutoConfiguration {
    protected static class EventConfiguration {
        @Bean
        public RouteAlarmEventSubscriber routeAlarmEventSubscriber() {
            return new RouteAlarmEventSubscriber();
        }
    }

    @Bean
    public EventPublisher eventPublisher() {
        return new EventPublisher();
    }

    @Bean
    public RouteAlarmEventSubscriber routeAlarmEventSubscriber() {
        return new RouteAlarmEventSubscriber();
    }
}