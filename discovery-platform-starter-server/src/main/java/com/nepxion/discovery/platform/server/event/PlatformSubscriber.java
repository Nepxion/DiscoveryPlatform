package com.nepxion.discovery.platform.server.event;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.eventbus.annotation.EventBus;

@EventBus
public class PlatformSubscriber {
    @Subscribe
    public void onAlarm(PlatformAlarmEvent platformAlarmEvent) {
        String alarmType = platformAlarmEvent.getAlarmType();

        if (StringUtils.equals(alarmType, "RouteInconsistency")) {
            System.out.println("::::: 推送告警信息给钉钉，告警类型=" + platformAlarmEvent.getAlarmType() + "，告警内容=" + platformAlarmEvent.getAlarmMap());
        }
    }

    @Subscribe
    public void onState(PlatformStateEvent platformStateEvent) {
        StateMessage<Events> message = platformStateEvent.getMessage();

        System.out.println("::::: 推送告警信息给钉钉，操作记录 :::::");
        System.out.println("业务对象：" + message.getHeaders());
        System.out.println("状态由【" + message.getSourceState().getFullDescription() + "】变迁为【" + message.getTargetState().getFullDescription() + "】，执行事件为【" + message.getPayload().getFullDescription() + "】");
        System.out.println("下一步可执行事件：" + message.getNextEvents().stream().map(event -> event.getFullDescription()).collect(Collectors.toList()));
        System.out.println("");
    }
}