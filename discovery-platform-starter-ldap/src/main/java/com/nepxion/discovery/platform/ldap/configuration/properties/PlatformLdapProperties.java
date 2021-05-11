package com.nepxion.discovery.platform.ldap.configuration.properties;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("platform.ldap")
public class PlatformLdapProperties {
    private String objectClassAttrName;
    private String loginIdAttrName;
    private String nameAttrName;
    private String mailAttrName;
    private String phoneNumberAttrName;
    private String titleAttrName;
    @Value("#{'${filter.memberOf:}'.split('\\|')}")
    private String[] memberOf;

    public String getObjectClassAttrName() {
        return objectClassAttrName;
    }

    public void setObjectClassAttrName(String objectClassAttrName) {
        this.objectClassAttrName = objectClassAttrName;
    }

    public String getLoginIdAttrName() {
        return loginIdAttrName;
    }

    public void setLoginIdAttrName(String loginIdAttrName) {
        this.loginIdAttrName = loginIdAttrName;
    }

    public String getNameAttrName() {
        return nameAttrName;
    }

    public void setNameAttrName(String nameAttrName) {
        this.nameAttrName = nameAttrName;
    }

    public String getMailAttrName() {
        return mailAttrName;
    }

    public void setMailAttrName(String mailAttrName) {
        this.mailAttrName = mailAttrName;
    }

    public String getPhoneNumberAttrName() {
        return phoneNumberAttrName;
    }

    public void setPhoneNumberAttrName(String phoneNumberAttrName) {
        this.phoneNumberAttrName = phoneNumberAttrName;
    }

    public String getTitleAttrName() {
        return titleAttrName;
    }

    public void setTitleAttrName(String titleAttrName) {
        this.titleAttrName = titleAttrName;
    }

    public String[] getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String[] memberOf) {
        this.memberOf = memberOf;
    }
}