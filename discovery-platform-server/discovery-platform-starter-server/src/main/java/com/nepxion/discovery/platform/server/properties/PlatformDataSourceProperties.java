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

@ConfigurationProperties("platform.datasource")
public class PlatformDataSourceProperties {
    private String type;
    private String initScriptPath = "META-INF/schema.sql";
    private boolean initScriptEnabled = false;
    private boolean initScriptLogger = false;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitScriptPath() {
        return initScriptPath;
    }

    public void setInitScriptPath(String initScriptPath) {
        this.initScriptPath = initScriptPath;
    }

    public boolean isInitScriptEnabled() {
        return initScriptEnabled;
    }

    public void setInitScriptEnabled(boolean initScriptEnabled) {
        this.initScriptEnabled = initScriptEnabled;
    }

    public boolean isInitScriptLogger() {
        return initScriptLogger;
    }

    public void setInitScriptLogger(boolean initScriptLogger) {
        this.initScriptLogger = initScriptLogger;
    }
}