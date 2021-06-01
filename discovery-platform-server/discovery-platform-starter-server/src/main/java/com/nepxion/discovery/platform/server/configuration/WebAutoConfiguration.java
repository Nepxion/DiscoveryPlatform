package com.nepxion.discovery.platform.server.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.Filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.nepxion.discovery.platform.server.converter.PlatformDateConverter;
import com.nepxion.discovery.platform.server.interceptor.LoginInterceptor;
import com.nepxion.discovery.platform.server.resolver.LoginArgumentResolver;

@Configuration
public class WebAutoConfiguration extends WebMvcConfigurationSupport {
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new ToStringSerializer(Long.TYPE));
        module.addSerializer(new ToStringSerializer(Long.class));
        module.addSerializer(new ToStringSerializer(BigInteger.class));
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(StringUtils.EMPTY);
            }
        });
        objectMapper.registerModule(module);
        converter.setObjectMapper(objectMapper);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public Filter filter() {
        return new ShallowEtagHeaderFilter();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:static/images/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:static/fonts/");
        registry.addResourceHandler("/layuiadmin/**").addResourceLocations("classpath:static/layuiadmin/");
        registry.addResourceHandler("/terminal/**").addResourceLocations("classpath:static/terminal/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:static/favicon.ico");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PlatformDateConverter());
        super.addFormatters(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**") // 拦截所有
                .excludePathPatterns("/") // 排除登录页面
                .excludePathPatterns("/css/**") // 排除拦截静态资源
                .excludePathPatterns("/js/**") // 排除拦截静态资源
                .excludePathPatterns("/images/**") // 排除拦截静态资源
                .excludePathPatterns("/fonts/**") // 排除拦截静态资源
                .excludePathPatterns("/layuiadmin/**") // 排除拦截静态资源
                .excludePathPatterns("/favicon.ico") // 排除拦截静态资源
                .excludePathPatterns("/login"); // 排除拦截登录逻辑
        super.addInterceptors(registry);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ServletWebArgumentResolverAdapter(new LoginArgumentResolver()));
        super.addArgumentResolvers(argumentResolvers);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(10);
    }
}