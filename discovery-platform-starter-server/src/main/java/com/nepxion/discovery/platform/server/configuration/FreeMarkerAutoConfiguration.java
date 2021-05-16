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

import com.nepxion.discovery.platform.server.template.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FreeMarkerAutoConfiguration implements InitializingBean {
    private final freemarker.template.Configuration freeMarkerConfiguration;

    public FreeMarkerAutoConfiguration(final freemarker.template.Configuration freeMarkerConfiguration) {
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public InsertDirective insertDirective() {
        return new InsertDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public NoInsertDirective noInsertDirective() {
        return new NoInsertDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public DeleteDirective deleteDirective() {
        return new DeleteDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public NoDeleteDirective noDeleteDirective() {
        return new NoDeleteDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public UpdateDirective updateDirective() {
        return new UpdateDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public NoUpdateDirective noUpdateDirective() {
        return new NoUpdateDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public SelectDirective selectDirective() {
        return new SelectDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public NoSelectDirective noSelectDirective() {
        return new NoSelectDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public OnlySelectDirective onlySelectDirective() {
        return new OnlySelectDirective();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public NotOnlySelectDirective notOnlySelectDirective() {
        return new NotOnlySelectDirective();
    }

    @Override
    public void afterPropertiesSet() {
        this.freeMarkerConfiguration.setSharedVariable("insert", insertDirective());
        this.freeMarkerConfiguration.setSharedVariable("delete", deleteDirective());
        this.freeMarkerConfiguration.setSharedVariable("update", updateDirective());
        this.freeMarkerConfiguration.setSharedVariable("select", selectDirective());

        this.freeMarkerConfiguration.setSharedVariable("no_insert", noInsertDirective());
        this.freeMarkerConfiguration.setSharedVariable("no_delete", noDeleteDirective());
        this.freeMarkerConfiguration.setSharedVariable("no_update", noUpdateDirective());
        this.freeMarkerConfiguration.setSharedVariable("no_select", noSelectDirective());

        this.freeMarkerConfiguration.setSharedVariable("only_select", onlySelectDirective());

        this.freeMarkerConfiguration.setSharedVariable("not_only_select", notOnlySelectDirective());
    }
}