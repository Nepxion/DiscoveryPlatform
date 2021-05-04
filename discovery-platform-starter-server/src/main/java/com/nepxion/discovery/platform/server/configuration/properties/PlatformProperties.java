package com.nepxion.discovery.platform.server.configuration.properties;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.enums.Mode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("platform.ui")
public class PlatformProperties {
    /**
     * 数据模式. 数据库模式:db; ldap模式: ldap; memory模式: 内存模式
     */
    private Mode mode = Mode.MEMORY;

    /**
     * 标题
     */
    private String title = "";

    /**
     * 应用全名称
     */
    private String fullName = "";

    /**
     * 应用简称
     */
    private String shortName = "";

    /**
     * ldap属性
     */
    private PlatformLdapProperties ldap;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

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

    public PlatformLdapProperties getLdap() {
        return ldap;
    }

    public void setLdap(PlatformLdapProperties ldap) {
        this.ldap = ldap;
    }
}
