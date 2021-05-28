package com.nepxion.discovery.platform.server.mysql.properties;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "platform.mysql")
public class PlatformMySqlProperties {
    /**
     * 数据库地址
     */
    private String host;

    /**
     * 数据库端口, 使用域名时, 可以为空
     */
    private String port;

    /**
     * 数据库名
     */
    private String name;

    /**
     * 数据库登陆名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 连接池最小连接数
     */
    private int minIdle;

    /**
     * 连接池最大连接数
     */
    private int maximum;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}