package com.nepxion.discovery.platform.server.entity.enums;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */

public enum GraphNodeType {

    ROOT("begin"),

    NORMAL("normal"),

    END("end"),
    ;

    String type;

    GraphNodeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
