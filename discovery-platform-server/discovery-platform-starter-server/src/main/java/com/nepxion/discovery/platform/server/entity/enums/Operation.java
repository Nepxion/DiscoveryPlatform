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
    DELETE(3, "DELETE"),
    SELECT(4, "SELECT");

    private int code;
    private String name;

    Operation(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Operation get(int code) {
        for (Operation item : Operation.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public static Operation get(String name) {
        for (Operation item : Operation.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}