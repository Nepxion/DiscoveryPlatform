package com.nepxion.discovery.platform.server.properties;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("platform.server")
public class PlatformServerProperties {
    /**
     * 标题
     */
    private String title = "Discovery Platform";

    /**
     * 应用全名称
     */
    private String fullName = "Discovery Platform";

    /**
     * 应用简称
     */
    private String shortName = "Discovery Platform";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}