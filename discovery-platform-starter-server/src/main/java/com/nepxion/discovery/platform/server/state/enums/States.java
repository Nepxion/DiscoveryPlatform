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
    STATE_INITIAL(10, "STATE_INITIAL", "初始"),
    STATE_AWAIT_ADD(11, "STATE_AWAIT_ADD", "待增加"),
    STATE_AWAIT_MODIFY(12, "STATE_AWAIT_MODIFY", "待修改"),
    STATE_AWAIT_DELETE(13, "STATE_AWAIT_DELETE", "待删除"),
    STATE_PUBLISHED(14, "STATE_PUBLISHED", "已发布");

    private int index;
    private String value;
    private String description;

    private States(int index, String value, String description) {
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