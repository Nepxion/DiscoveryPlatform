package com.nepxion.discovery.platform.server.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import com.nepxion.discovery.platform.server.tool.ExceptionTool;

public class PlatformEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication springApplication) {
        if (configurableEnvironment == null || springApplication == null) {
            return;
        }
        WebApplicationType webApplicationType = springApplication.getWebApplicationType();
        if (webApplicationType == null || webApplicationType == WebApplicationType.NONE) {
            return;
        }

        Properties properties = new Properties();

        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.allow-request-override", true);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.check-template-location", true);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.suffix", ".ftl");
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.content-type", "text/html;charset=utf-8");
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.enabled", true);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.cache", false);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.template-loader-path", "classpath:/templates/");
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.prefer-file-system-access", false);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.charset", "UTF-8");
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.expose-request-attributes", true);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.expose-session-attributes", true);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.expose-spring-macro-helpers", true);
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.request-context-attribute", "request");
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.settings.auto_import", "common/spring.ftl as spring");
        this.addDefaultConfig(configurableEnvironment, properties, "spring.freemarker.settings.number_format", "0.##");
        this.addDefaultConfig(configurableEnvironment, properties, "spring.main.allow-bean-definition-overriding", true);

        if (!properties.isEmpty()) {
            configurableEnvironment.getPropertySources().addFirst(new PropertiesPropertySource("springCloudApplicationProperties", properties));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private void addDefaultConfig(ConfigurableEnvironment configurableEnvironment, Properties properties, String name, Object value) {
        try {
            String oldProperty = configurableEnvironment.getProperty(name);
            if (StringUtils.isEmpty(oldProperty)) {
                properties.put(name, value);
            }
        } catch (IllegalArgumentException exception) {
            LOG.error(ExceptionTool.getRootCauseMessage(exception), exception);
        }
    }
}