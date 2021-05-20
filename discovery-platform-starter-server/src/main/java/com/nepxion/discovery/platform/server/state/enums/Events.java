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
    // 事件：执行增加
    EVENT_DO_ADD(20, "EVENT_DO_ADD"),
    // 事件：执行修改
    EVENT_DO_MODIFY(21, "EVENT_DO_MODIFY"),
    // 事件：执行删除
    EVENT_DO_DELETE(22, "EVENT_DO_DELETE"),
    // 事件：执行回滚
    EVENT_DO_ROLLBACK(23, "EVENT_DO_ROLLBACK"),
    // 事件：执行发布
    EVENT_DO_PUBLISH(24, "EVENT_DO_PUBLISH");

    private int index;
    private String value;

    private Events(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
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