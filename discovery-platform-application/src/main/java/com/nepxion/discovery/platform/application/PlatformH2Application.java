package com.nepxion.discovery.platform.application;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @author Haojun Ren
 * @version 1.0
 */

import de.codecentric.boot.admin.server.config.EnableAdminServer;

import org.springframework.boot.SpringApplication;

import com.nepxion.discovery.platform.server.annotation.EnablePlatformServer;

@EnablePlatformServer
@EnableAdminServer
public class PlatformH2Application {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "h2");

        SpringApplication.run(PlatformH2Application.class, args);
    }
}