package com.nepxion.discovery.platform.server.adapter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.platform.server.event.PlatformPublisher;
import com.nepxion.discovery.platform.server.event.PlatformStateEvent;
import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.handler.StateChangeAdapter;

public class PlatformStateChangeAdapter implements StateChangeAdapter {
    @Autowired
    private PlatformPublisher platformPublisher;

    @Override
    public void onChanged(StateMessage<Events> message) {
        platformPublisher.asyncPublish(new PlatformStateEvent(message));
    }
}