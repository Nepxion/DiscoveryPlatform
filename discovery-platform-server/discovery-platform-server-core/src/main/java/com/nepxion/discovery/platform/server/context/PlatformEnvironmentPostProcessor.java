package com.nepxion.discovery.platform.server.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.ObjectUtils;

import com.nepxion.discovery.platform.server.tool.ExceptionTool;

import java.util.Properties;

public class PlatformEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment configurableEnvironment,
                                       final SpringApplication springApplication) {
        if (null == configurableEnvironment || null == springApplication) {
            return;
        }
        final WebApplicationType webApplicationType = springApplication.getWebApplicationType();
        if (null == webApplicationType || WebApplicationType.NONE == webApplicationType) {
            return;
        }

        final Properties properties = new Properties();

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

    private void addDefaultConfig(final ConfigurableEnvironment configurableEnvironment,
                                  final Properties properties,
                                  final String name,
                                  final Object value) {
        try {
            final String oldProperty = configurableEnvironment.getProperty(name);
            if (ObjectUtils.isEmpty(oldProperty)) {
                properties.put(name, value);
            }
        } catch (final IllegalArgumentException exception) {
            LOG.error(ExceptionTool.getRootCauseMessage(exception), exception);
        }
    }
}