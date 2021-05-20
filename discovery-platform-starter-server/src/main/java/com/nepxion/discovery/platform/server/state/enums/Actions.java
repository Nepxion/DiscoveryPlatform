package com.nepxion.discovery.platform.server.state.enums;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public enum Actions {
    // 动作：去增加
    ACTION_TO_ADD(30, "ACTION_TO_ADD"),
    // 动作：去修改
    ACTION_TO_MODIFY(31, "ACTION_TO_MODIFY"),
    // 动作：去删除
    ACTION_TO_DELETE(32, "ACTION_TO_DELETE"),
    // 动作：去回滚
    ACTION_TO_ROLLBACK(33, "ACTION_TO_ROLLBACK"),
    // 动作：去发布
    ACTION_TO_PUBLISH(34, "ACTION_TO_PUBLISH");

    private int index;
    private String value;

    private Actions(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public static Actions fromString(int index) {
        for (Actions type : Actions.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }

        throw new IllegalArgumentException("No matched type with index=" + index);
    }

    public static Actions fromString(String value) {
        for (Actions type : Actions.values()) {
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