package com.nepxion.discovery.platform.server.mysql.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.discovery.platform.server.adapter.PlatformLoginAdapter;
import com.nepxion.discovery.platform.server.mysql.adapter.PlatformMySqlLoginAdapter;
import com.nepxion.discovery.platform.server.mysql.constant.PlatformMySqlConstant;
import com.taobao.text.Color;

@Configuration
@ConditionalOnProperty(name = "platform.datasource.type", havingValue = PlatformMySqlConstant.MYSQL_TYPE)
public class PlatformMySqlAutoConfiguration {
    static {
        LogoBanner logoBanner = new LogoBanner(PlatformMySqlAutoConfiguration.class, "/com/nepxion/mysql/resource/logo.txt", "Welcome to Nepxion", 5, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow }, true);
        NepxionBanner.show(logoBanner, new Description("Plugin:", PlatformMySqlConstant.MYSQL_TYPE, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Discovery", 0, 1));
    }

    @Bean
    public PlatformLoginAdapter platformLoginAdapter() {
        return new PlatformMySqlLoginAdapter();
    }
}