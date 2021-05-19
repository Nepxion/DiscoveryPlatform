package com.nepxion.discovery.platform.client.event;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.google.common.eventbus.Subscribe;
import com.nepxion.discovery.plugin.strategy.event.StrategyAlarmEvent;
import com.nepxion.eventbus.annotation.EventBus;

@EventBus
public class PlatformAlarmEventSubscriber {
    @Subscribe
    public void onAlarm(StrategyAlarmEvent strategyAlarmEvent) {
        System.out.println("::::: 推送告警信息给钉钉，告警类型=" + strategyAlarmEvent.getAlarmType() + "，告警内容=" + strategyAlarmEvent.getAlarmMap());
    }
}