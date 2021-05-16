package com.nepxion.discovery.platform.server.event;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.common.event.AlarmEvent;

public class PlatformEventWapper {
    @Autowired
    private PlatformPublisher platformPublisher;

    public void fireAlarm(AlarmEvent alarmEvent) {
        platformPublisher.asyncPublish(alarmEvent);
    }
}