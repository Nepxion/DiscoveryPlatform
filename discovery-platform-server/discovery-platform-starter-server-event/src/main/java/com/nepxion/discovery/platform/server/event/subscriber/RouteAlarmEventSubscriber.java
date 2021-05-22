package com.nepxion.discovery.platform.server.event.subscriber;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.nepxion.eventbus.annotation.EventBus;

@EventBus
public class RouteAlarmEventSubscriber {
    @Subscribe
    public void onAlarm(RouteAlarmEvent platformAlarmEvent) {
        String alarmType = platformAlarmEvent.getAlarmType();

        if (StringUtils.equals(alarmType, "RouteInconsistency")) {
            System.out.println("::::: 推送告警信息给钉钉，告警类型=" + platformAlarmEvent.getAlarmType() + "，告警内容=" + platformAlarmEvent.getAlarmMap());
        }
    }
}