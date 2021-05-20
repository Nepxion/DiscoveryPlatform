package com.nepxion.discovery.platform.server.event;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.Serializable;

import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;

public class PlatformStateEvent implements Serializable {
    private static final long serialVersionUID = 1151369261491991316L;

    private StateMessage<Events> message;

    public PlatformStateEvent(StateMessage<Events> message) {
        this.message = message;
    }

    public StateMessage<Events> getMessage() {
        return message;
    }
}