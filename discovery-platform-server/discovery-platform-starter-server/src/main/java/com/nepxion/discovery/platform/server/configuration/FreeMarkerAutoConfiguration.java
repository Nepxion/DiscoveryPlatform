package com.nepxion.discovery.platform.server.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.nepxion.discovery.platform.server.template.DeleteDirective;
import com.nepxion.discovery.platform.server.template.InsertDirective;
import com.nepxion.discovery.platform.server.template.NoDeleteDirective;
import com.nepxion.discovery.platform.server.template.NoInsertDirective;
import com.nepxion.discovery.platform.server.template.NoSelectDirective;
import com.nepxion.discovery.platform.server.template.NoUpdateDirective;
import com.nepxion.discovery.platform.server.template.NotOnlySelectDirective;
import com.nepxion.discovery.platform.server.template.OnlySelectDirective;
import com.nepxion.discovery.platform.server.template.SelectDirective;
import com.nepxion.discovery.platform.server.template.UpdateDirective;

@Configuration
public class FreeMarkerAutoConfiguration implements InitializingBean {
    @Autowired
    private freemarker.template.Configuration freeMarkerConfiguration;

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
        freeMarkerConfiguration.setSharedVariable("insert", insertDirective());
        freeMarkerConfiguration.setSharedVariable("delete", deleteDirective());
        freeMarkerConfiguration.setSharedVariable("update", updateDirective());
        freeMarkerConfiguration.setSharedVariable("select", selectDirective());
        freeMarkerConfiguration.setSharedVariable("no_insert", noInsertDirective());
        freeMarkerConfiguration.setSharedVariable("no_delete", noDeleteDirective());
        freeMarkerConfiguration.setSharedVariable("no_update", noUpdateDirective());
        freeMarkerConfiguration.setSharedVariable("no_select", noSelectDirective());
        freeMarkerConfiguration.setSharedVariable("only_select", onlySelectDirective());
        freeMarkerConfiguration.setSharedVariable("not_only_select", notOnlySelectDirective());
    }
}