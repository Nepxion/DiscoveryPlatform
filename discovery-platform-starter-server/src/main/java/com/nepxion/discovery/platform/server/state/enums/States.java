package com.nepxion.discovery.platform.server.state.enums;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public enum States {
    // 状态：初始
    STATE_INITIAL(10, "STATE_INITIAL"),
    // 状态：待增加
    STATE_TO_ADD(11, "STATE_TO_ADD"),
    // 状态：待修改
    STATE_TO_MODIFY(12, "STATE_TO_MODIFY"),
    // 状态：待删除
    STATE_TO_DELETE(13, "STATE_TO_DELETE"),
    // 状态：已发布
    STATE_PUBLISHED(14, "STATE_PUBLISHED");

    private int index;
    private String value;

    private States(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public static States fromString(int index) {
        for (States type : States.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }

        throw new IllegalArgumentException("No matched type with index=" + index);
    }

    public static States fromString(String value) {
        for (States type : States.values()) {
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