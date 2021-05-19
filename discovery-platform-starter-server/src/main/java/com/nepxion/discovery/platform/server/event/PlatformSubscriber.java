package com.nepxion.discovery.platform.server.event;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.nepxion.eventbus.annotation.EventBus;

@EventBus
public class PlatformSubscriber {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformSubscriber.class);

    @Subscribe
    public void onAlarm(PlatformAlarmEvent platformAlarmEvent) {
        String alarmType = platformAlarmEvent.getAlarmType();

        LOG.info("Alarm event has been triggered, event type={}", alarmType);

        if (StringUtils.equals(alarmType, "RouteInconsistency")) {
            System.out.println("::::: 推送告警信息给钉钉，告警类型=" + platformAlarmEvent.getAlarmType() + "，告警内容=" + platformAlarmEvent.getAlarmMap());
        }
    }
}