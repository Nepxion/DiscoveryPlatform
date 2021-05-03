package com.nepxion.discovery.platform.server.enums;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public enum Mode {
    DB(1, "db"),
    LDAP(2, "ldap"),
    MEMORY(3, "memory");

    private final int code;
    private final String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    Mode(final int code,
         final String name) {
        this.code = code;
        this.name = name;
    }

    public static Mode get(final Integer code) {
        for (final Mode item : Mode.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static Mode get(final String name) {
        for (final Mode item : Mode.values()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}