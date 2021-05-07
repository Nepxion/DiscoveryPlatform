package com.nepxion.discovery.platform.server.ui.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */


import com.nepxion.discovery.platform.server.ui.constant.PlatformConstant;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {PlatformConstant.BASE_PACKAGE_NAME})
public class ComponentRegister {
}