package com.nepxion.discovery.platform.server.ui.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.discovery.platform.server.ui.configuration.PlatformAutoConfiguration;
import com.nepxion.discovery.platform.server.ui.constant.PlatformConstant;
import com.taobao.text.Color;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PlatformApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
            LogoBanner logoBanner = new LogoBanner(PlatformAutoConfiguration.class, "/com/nepxion/platform/resource/logo.txt", "Welcome to Nepxion", 8, 5, new Color[]{Color.red, Color.green, Color.cyan, Color.blue, Color.yellow, Color.magenta, Color.red, Color.green}, true);

            NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", PlatformConstant.PLATFORM_VERSION, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Discovery", 0, 1));
        }
    }
}