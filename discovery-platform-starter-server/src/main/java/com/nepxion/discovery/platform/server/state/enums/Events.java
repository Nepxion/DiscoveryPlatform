package com.nepxion.discovery.platform.server.state.enums;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public enum Events {
    EVENT_DO_ADD(20, "EVENT_DO_ADD", "增加"),
    EVENT_DO_MODIFY(21, "EVENT_DO_MODIFY", "修改"),
    EVENT_DO_DELETE(22, "EVENT_DO_DELETE", "删除"),
    EVENT_DO_ROLLBACK(23, "EVENT_DO_ROLLBACK", "回滚"),
    EVENT_DO_PUBLISH(24, "EVENT_DO_PUBLISH", "发布");

    private int index;
    private String value;
    private String description;

    private Events(int index, String value, String description) {
        this.index = index;
        this.value = value;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getFullDescription() {
        return description + ":" + value;
    }

    public static Events fromString(int index) {
        for (Events type : Events.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }

        throw new IllegalArgumentException("No matched type with index=" + index);
    }

    public static Events fromString(String value) {
        for (Events type : Events.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("No matched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}