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

public enum Operation {
    INSERT(1, "INSERT"),
    UPDATE(2, "UPDATE"),
    DELETE(3, "DELETE");

    private final int code;
    private final String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    Operation(final int code,
              final String name) {
        this.code = code;
        this.name = name;
    }

    public static Operation get(final Integer code) {
        for (final Operation item : Operation.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static Operation get(final String name) {
        for (final Operation item : Operation.values()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}