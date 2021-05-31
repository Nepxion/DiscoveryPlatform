package com.nepxion.discovery.platform.server.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Haojun Ren
 * @author Ning Zhang
 * @version 1.0
 */

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.properties.PlatformServerProperties;
import com.taobao.text.Color;

public class PlatformApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformApplicationContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
            LogoBanner logoBanner = new LogoBanner(PlatformApplicationContextInitializer.class, "/com/nepxion/platform/resource/logo.txt", "Welcome to Nepxion", 8, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow, Color.magenta, Color.red, Color.green }, true);

            NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", PlatformConstant.PLATFORM_VERSION, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Discovery", 0, 1));
        }

        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof DataSourceProperties) {
                    PlatformServerProperties platformServerProperties = applicationContext.getBean(PlatformServerProperties.class);
                    if (platformServerProperties.isInitScriptEnabled()) {
                        DataSourceProperties dataSourceProperties = (DataSourceProperties) bean;

                        initializeScript(dataSourceProperties, platformServerProperties);
                    }
                }

                return super.postProcessAfterInitialization(bean, beanName);
            }
        });
    }

    private void initializeScript(DataSourceProperties dataSourceProperties, PlatformServerProperties platformServerProperties) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());

            ScriptRunner scriptRunner = new ScriptRunner(connection);
            if (!platformServerProperties.isInitScriptLogger()) {
                scriptRunner.setLogWriter(null);
            }

            String initScriptPath = platformServerProperties.getInitScriptPath();

            Resources.setCharset(StandardCharsets.UTF_8);
            Reader reader = Resources.getResourceAsReader(initScriptPath);

            LOG.info("----- Initialize Platform Script Information -----");

            LOG.info("Execute platform script path: {}", initScriptPath);

            scriptRunner.runScript(reader);

            LOG.info("--------------------------------------------------");
        } catch (SQLException e) {
            LOG.error("Failed to get Connection", e);
        } catch (IOException e) {
            LOG.error("Failed to get script", e);
        } catch (Exception e) {
            LOG.error("Failed to initialize database script", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("Failed to close Connection", e);
                }
            }
        }
    }
}