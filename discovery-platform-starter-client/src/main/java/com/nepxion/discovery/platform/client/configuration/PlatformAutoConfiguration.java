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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.nepxion.discovery.common.apollo.proccessor.ApolloProcessor;
import com.nepxion.discovery.common.consul.proccessor.ConsulProcessor;
import com.nepxion.discovery.common.etcd.proccessor.EtcdProcessor;
import com.nepxion.discovery.common.nacos.proccessor.NacosProcessor;
import com.nepxion.discovery.common.redis.proccessor.RedisProcessor;
import com.nepxion.discovery.common.zookeeper.proccessor.ZookeeperProcessor;
import com.nepxion.discovery.platform.client.route.PlatformGatewayRouteApolloProcessor;
import com.nepxion.discovery.platform.client.route.PlatformGatewayRouteConsulProcessor;
import com.nepxion.discovery.platform.client.route.PlatformGatewayRouteEtcdProcessor;
import com.nepxion.discovery.platform.client.route.PlatformGatewayRouteNacosProcessor;
import com.nepxion.discovery.platform.client.route.PlatformGatewayRouteRedisProcessor;
import com.nepxion.discovery.platform.client.route.PlatformGatewayRouteZookeeperProcessor;
import com.nepxion.discovery.platform.client.route.PlatformZuulRouteApolloProcessor;
import com.nepxion.discovery.platform.client.route.PlatformZuulRouteConsulProcessor;
import com.nepxion.discovery.platform.client.route.PlatformZuulRouteEtcdProcessor;
import com.nepxion.discovery.platform.client.route.PlatformZuulRouteNacosProcessor;
import com.nepxion.discovery.platform.client.route.PlatformZuulRouteRedisProcessor;
import com.nepxion.discovery.platform.client.route.PlatformZuulRouteZookeeperProcessor;
import com.nepxion.discovery.plugin.strategy.gateway.route.GatewayStrategyRoute;
import com.nepxion.discovery.plugin.strategy.zuul.route.ZuulStrategyRoute;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class PlatformAutoConfiguration {
    @ConditionalOnBean(GatewayStrategyRoute.class)
    @ConditionalOnClass(NacosProcessor.class)
    protected static class GatewayRouteNacosConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public NacosProcessor nacosProcessor() {
            return new PlatformGatewayRouteNacosProcessor();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    @ConditionalOnClass(ApolloProcessor.class)
    protected static class GatewayRouteApolloConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public ApolloProcessor apolloProcessor() {
            return new PlatformGatewayRouteApolloProcessor();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    @ConditionalOnClass(RedisProcessor.class)
    protected static class GatewayRouteRedisConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public RedisProcessor redisProcessor() {
            return new PlatformGatewayRouteRedisProcessor();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    @ConditionalOnClass(ZookeeperProcessor.class)
    protected static class GatewayRouteZookeeperConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public ZookeeperProcessor zookeeperProcessor() {
            return new PlatformGatewayRouteZookeeperProcessor();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    @ConditionalOnClass(ConsulProcessor.class)
    protected static class GatewayRouteConsulConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public ConsulProcessor consulProcessor() {
            return new PlatformGatewayRouteConsulProcessor();
        }
    }

    @ConditionalOnBean(GatewayStrategyRoute.class)
    @ConditionalOnClass(EtcdProcessor.class)
    protected static class GatewayRouteEtcdConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.gateway.discovery.locator.enabled", havingValue = "false", matchIfMissing = true)
        public EtcdProcessor etcdProcessor() {
            return new PlatformGatewayRouteEtcdProcessor();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    @ConditionalOnClass(NacosProcessor.class)
    protected static class ZuulRouteNacosConfiguration {
        @Bean
        public NacosProcessor nacosProcessor() {
            return new PlatformZuulRouteNacosProcessor();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    @ConditionalOnClass(ApolloProcessor.class)
    protected static class ZuulRouteApolloConfiguration {
        @Bean
        public ApolloProcessor apolloProcessor() {
            return new PlatformZuulRouteApolloProcessor();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    @ConditionalOnClass(RedisProcessor.class)
    protected static class ZuulRouteRedisConfiguration {
        @Bean
        public RedisProcessor redisProcessor() {
            return new PlatformZuulRouteRedisProcessor();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    @ConditionalOnClass(ZookeeperProcessor.class)
    protected static class ZuulRouteZookeeperConfiguration {
        @Bean
        public ZookeeperProcessor zookeeperProcessor() {
            return new PlatformZuulRouteZookeeperProcessor();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    @ConditionalOnClass(ConsulProcessor.class)
    protected static class ZuulRouteConsulConfiguration {
        @Bean
        public ConsulProcessor consulProcessor() {
            return new PlatformZuulRouteConsulProcessor();
        }
    }

    @ConditionalOnBean(ZuulStrategyRoute.class)
    @ConditionalOnClass(EtcdProcessor.class)
    protected static class ZuulRouteEtcdConfiguration {
        @Bean
        public EtcdProcessor etcdProcessor() {
            return new PlatformZuulRouteEtcdProcessor();
        }
    }
}