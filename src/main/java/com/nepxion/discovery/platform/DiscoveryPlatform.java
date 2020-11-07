package com.nepxion.discovery.platform;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DiscoveryPlatform {
    public static void main(String[] args) {
        // 彩色旗标显示设置
        System.setProperty("nepxion.banner.shown.ansi.mode", "true");

        new SpringApplicationBuilder(DiscoveryPlatform.class).run(args);
    }
}