package com.nepxion.discovery.platform.server.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * 
 * @author Haojun Ren
 * @version 1.0
 */

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.constant.DiscoverySwaggerConstant;

@Configuration
@ConditionalOnProperty(value = DiscoverySwaggerConstant.SWAGGER_SERVICE_ENABLED, matchIfMissing = true)
public class SwaggerAutoConfiguration {
    @Bean
    public List<Parameter> swaggerHeaderParameters() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name(DiscoveryConstant.N_D_ACCESS_TOKEN).description("Access Token。格式：" + DiscoveryConstant.BEARER + " access-token。中间含空格").modelRef(new ModelRef("string")).parameterType("header").defaultValue(DiscoveryConstant.BEARER + " ").required(true).build();

        return Collections.singletonList(parameterBuilder.build());
    }

    @Bean
    public List<ApiKey> swaggerSecuritySchemes() {
        return Collections.singletonList(new ApiKey(DiscoveryConstant.N_D_ACCESS_TOKEN, DiscoveryConstant.N_D_ACCESS_TOKEN, "header"));
    }

    @Bean
    public List<SecurityContext> swaggerSecurityContexts() {
        return Collections.singletonList(
                SecurityContext
                        .builder()
                        .securityReferences(Collections.singletonList(new SecurityReference(DiscoveryConstant.N_D_ACCESS_TOKEN, scopes())))
                        .forPaths(PathSelectors.any())
                        .build());
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[] { new AuthorizationScope("global", "accessAnything") };
    }
}