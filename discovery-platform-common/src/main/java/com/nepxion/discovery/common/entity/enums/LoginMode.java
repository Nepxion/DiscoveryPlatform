package com.nepxion.discovery.common.entity.enums;

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
    DB(1, "db"),
    LDAP(2, "ldap");

    private final int code;
    private final String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    LoginMode(final int code,
              final String name) {
        this.code = code;
        this.name = name;
    }

    public static LoginMode get(final Integer code) {
        for (final LoginMode item : LoginMode.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static LoginMode get(final String name) {
        for (final LoginMode item : LoginMode.values()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}