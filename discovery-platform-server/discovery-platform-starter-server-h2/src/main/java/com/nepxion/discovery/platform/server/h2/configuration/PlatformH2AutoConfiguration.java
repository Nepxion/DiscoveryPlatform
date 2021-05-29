package com.nepxion.discovery.platform.server.h2.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.discovery.platform.server.h2.constant.PlatformH2Constant;
import com.nepxion.discovery.platform.server.h2.properties.PlatformH2Properties;
import com.taobao.text.Color;

@Configuration
@EnableConfigurationProperties({ PlatformH2Properties.class })
public class PlatformH2AutoConfiguration {
    static {
        LogoBanner logoBanner = new LogoBanner(PlatformH2AutoConfiguration.class, "/com/nepxion/h2/resource/logo.txt", "Welcome to Nepxion", 9, 5, new Color[]{Color.red, Color.green}, true);
        NepxionBanner.show(logoBanner, new Description("Plugin:", PlatformH2Constant.H2_TYPE, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Discovery", 0, 1));
    }
}