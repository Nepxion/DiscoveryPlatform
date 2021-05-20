package com.nepxion.discovery.platform.server.state.handler;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;

public interface StateChangeAdapter {
    void onChanged(StateMessage<Events> message);
}