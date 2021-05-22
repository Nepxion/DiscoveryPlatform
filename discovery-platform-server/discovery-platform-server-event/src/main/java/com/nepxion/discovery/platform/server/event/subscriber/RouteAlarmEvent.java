package com.nepxion.discovery.platform.server.event.subscriber;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Map;

public class RouteAlarmEvent implements Serializable {
    private static final long serialVersionUID = 5966845230262521754L;

    private String alarmType;
    private Map<String, String> alarmMap;

    // 自行修改模型
    public RouteAlarmEvent(String alarmType, Map<String, String> alarmMap) {
        this.alarmType = alarmType;
        this.alarmMap = alarmMap;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public Map<String, String> getAlarmMap() {
        return alarmMap;
    }
}