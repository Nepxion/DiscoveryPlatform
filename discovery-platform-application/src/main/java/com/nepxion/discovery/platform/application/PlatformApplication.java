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

import org.springframework.boot.SpringApplication;

import com.nepxion.discovery.platform.server.annotation.EnablePlatformServer;
import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnablePlatformServer
@EnableAdminServer
public class PlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }
}