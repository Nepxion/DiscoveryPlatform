package com.nepxion.discovery.platform.server.entity.enums;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public enum LoginMode {
    DATABASE(1, "database"),
    LDAP(2, "ldap");

    private int code;
    private String name;

    LoginMode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static LoginMode get(int code) {
        for (LoginMode item : LoginMode.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static LoginMode get(String name) {
        for (LoginMode item : LoginMode.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}